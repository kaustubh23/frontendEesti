package eesti.frontend.demo.modelview;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import eesti.assignment.model.Itemdetails;
import eesti.frontend.demo.ssf.ItemsClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@VariableResolver(DelegatingVariableResolver.class)
public class MainViewModel {

	private ItemsClient itemsClient;

	// Map<String, PageModel<String>> pages = new HashMap<>();
//	private PageModel<String> currentPage;
	private List<Itemdetails> itemsList;
	private double cashPaid;
	private double totalPrice = 0;
	private double returnCash;
	private boolean submitEntry;
	@Autowired
	private Itemdetails item = new Itemdetails();

	@Init
	public void init() {
		itemsList = new ArrayList<>();
		itemsClient = new ItemsClient();
		Mono<List<Itemdetails>> mono = itemsClient.getItems().collectList();
		setItemList(mono.block());
		checkQuantityLessThanzero();
	}
	/*
	 * @Listen(Events.ON_SELECT + "= #listclick") public void
	 * onListClick(@SuppressWarnings("rawtypes") Event event) { if (getItemsList()
	 * != null) { int orderQuantity =
	 * getItemsList().get(getItemsList().indexOf(item)).getOrderQuntity();
	 * getItemsList().get(getItemsList().indexOf(item)).setOrderQuntity(
	 * orderQuantity + 1); ; }
	 * 
	 * }
	 */

	@Command
	@NotifyChange({ "returnCash" })
	public void process() {
		/*
		 * if (getItemsList() != null) { getItemsList().stream().forEach(item -> { if
		 * (item.getOrderQuntity() > 0) { totalPrice = item.getPrice() *
		 * item.getOrderQuntity(); } }); }
		 */
		setReturnCash(cashPaid - totalPrice);
		Flux<Itemdetails> flux = itemsClient.buyItem(getItemsList());
		Mono<List<Itemdetails>> mom = flux.collectList();
		List<Itemdetails> get = mom.block();

	}

	@Command
	@NotifyChange({ "captureClick" })
	public void capture() {
		/*
		 * if (getItemsList() != null) { getItemsList().stream().forEach(item -> { if
		 * (item.getOrderQuntity() > 0) { totalPrice = item.getPrice() *
		 * item.getOrderQuntity(); } }); }
		 */
		if (getItemsList() != null) {
			getItemsList().stream().forEach(p -> {
				p.setQuantity(p.getQuantityInsert());
				p.setEntry(true);
			});
		}
		itemsClient.buyItem(getItemsList()).subscribe();
		 init();
	}

	/*
	 * @Command
	 * 
	 * @NotifyChange({ "itemsList" }) public void entryClick() { List<Itemdetails>
	 * list =getItemsList(); if (list != null) { list.stream().forEach(p->{
	 * 
	 * if(!p.isEntry()) { setSubmitEntry(true);
	 * 
	 * } }); } }
	 */

	@Command
	@NotifyChange({ "itemsList" })
	public void imageClick() {

		if (getItemsList() != null) {
			int orderQuantity = getItemsList().get(getItemsList().indexOf(item)).getOrderQuntity();
			int quantity = getItemsList().get(getItemsList().indexOf(item)).getQuantity();
			getItemsList().get(getItemsList().indexOf(item)).setOrderQuntity(orderQuantity + 1);
			getItemsList().get(getItemsList().indexOf(item)).setQuantity(quantity - 1);
			;
		}

		if (getItemsList() != null) {
			getItemsList().stream().forEach(item -> {
				if (item.getOrderQuntity() > 0) {
					totalPrice = item.getPrice() * item.getOrderQuntity();
				}
			});
		}
		
		checkQuantityLessThanzero();
	}
	
	public void checkQuantityLessThanzero() {
		
		if (getItemsList() != null) {
			getItemsList().stream().forEach(item -> {
				if (item.getOrderQuntity() > 0) {
					totalPrice = item.getPrice() * item.getOrderQuntity();
				}
				if(item.getQuantity()<=0) {
					item.setImgDisabled(true);
				}else {
					item.setImgDisabled(false);
				}
			});
		}
	}
	

	/*
	 * @Command
	 * 
	 * @NotifyChange("currentTime") public void updateTime() {
	 * 
	 * //NOOP just for the notify change }
	 */

	/*
	 * @Command
	 * 
	 * @NotifyChange("currentPage") public void navigate(@BindingParam("page")
	 * String page) { this.currentPage = pages.get(page); }
	 */

	@Command
	@NotifyChange("itemsList")
	public void setItemList(@BindingParam("itemsList") List<Itemdetails> itemsList) {
		this.itemsList = itemsList;
	}

	public List<Itemdetails> getItemsList() {
		return itemsList;
	}

	public Itemdetails getItem() {
		return item;
	}

	@Command
	@NotifyChange("item")
	public void setItem(@BindingParam("item") Itemdetails item) {
		this.item = item;
	}

	public double getCashPaid() {
		return cashPaid;
	}

	@Command
	@NotifyChange("cashPaid")
	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}

	public double getReturnCash() {
		return returnCash;
	}

	@Command
	@NotifyChange("returnCash")
	public void setReturnCash(double returnCash) {
		this.returnCash = returnCash;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	@Command
	@NotifyChange("totalPrice")
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public boolean isSubmitEntry() {
		return submitEntry;
	}

	@Command
	@NotifyChange("submitEntry")
	public void setSubmitEntry(boolean submitEntry) {
		this.submitEntry = submitEntry;
	}
}
