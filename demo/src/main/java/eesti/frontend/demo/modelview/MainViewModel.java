package eesti.frontend.demo.modelview;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import eesti.assignment.model.Itemdetails;
import eesti.frontend.demo.ssf.ItemsClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@VariableResolver(DelegatingVariableResolver.class)
public class MainViewModel {

	private ItemsClient itemsClient;
	private List<Itemdetails> itemsList;
	private double cashPaid;
	private double returnTotal;
	private double returnCash;
	private boolean submitEntry;
	private boolean captureDisabled;
	double total = 0;
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

	@Command
	@NotifyChange({ "returnCash" })
	public void process() {

		Flux<Itemdetails> flux = itemsClient.buyItem(getItemsList());
		Mono<List<Itemdetails>> mom = flux.collectList();
		List<Itemdetails> get = mom.block();
		// Executions.getCurrent().sendRedirect("");
		setReturnCash(cashPaid - total);
		

	}

	@Command
	@NotifyChange({ "captureClick" })
	public void capture() {
		if (getItemsList() != null) {
			getItemsList().stream().forEach(p -> {
				p.setQuantity(p.getQuantityInsert());
				p.setEntry(true);
			});
		}
		itemsClient.buyItem(getItemsList()).subscribe();
		setSubmitEntry(false);
		Executions.getCurrent().sendRedirect("");
	}

	@Command
	@NotifyChange({ "itemsList" })
	public void imageClick() {
		checkQuantityLessThanzero();
		if (getItemsList() != null) {
			if (!item.isImgDisabled()) {
				int orderQuantity = getItemsList().get(getItemsList().indexOf(item)).getOrderQuntity();
				int quantity = getItemsList().get(getItemsList().indexOf(item)).getQuantity();
				getItemsList().get(getItemsList().indexOf(item)).setOrderQuntity(orderQuantity + 1);
				getItemsList().get(getItemsList().indexOf(item)).setQuantity(quantity - 1);

			}

		}

	}

	public void checkQuantityLessThanzero() {
		 DecimalFormat df = new DecimalFormat("0.00");
		if (getItemsList() != null) {
			for (Itemdetails item : getItemsList()) {
				if (!item.isEntry()) {
					setCaptureDisabled(false);
				} else {
					setCaptureDisabled(true);
				}
				if (item.getOrderQuntity() > 0) {
					total = total + (double) (item.getPrice() * item.getOrderQuntity());

					if (item.getQuantity() <= 0) {
						item.setImgDisabled(true);
					} else {
						item.setImgDisabled(false);
					}
				}
			}

		}
		setReturnTotal(Double.parseDouble(df.format(total)));

		// setTotalAmount(String.valueOf(total));

	}

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
	  
	  public void setReturnCash(double returnCash) {
	  this.returnCash = returnCash; 
	  BindUtils.postNotifyChange(null, null, this, "returnCash");}
	  
	  public boolean isSubmitEntry() { return submitEntry; }
	 
	@Command
	@NotifyChange("submitEntry")
	public void setSubmitEntry(boolean submitEntry) {
		this.submitEntry = submitEntry;
	}

	public boolean isCaptureDisabled() {
		return captureDisabled;
	}

	public void setCaptureDisabled(boolean captureDisabled) {
		this.captureDisabled = captureDisabled;
	}

	public double getReturnTotal() {
		return returnTotal;
	}
	
	public void setReturnTotal(double returnTotal) {
		this.returnTotal = returnTotal;
		BindUtils.postNotifyChange(null, null, this, "returnTotal");
	}

}
