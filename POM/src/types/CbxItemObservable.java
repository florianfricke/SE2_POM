package types;

import javafx.beans.binding.ObjectBinding;

public class CbxItemObservable extends ObjectBinding<CbxItem> {
	private final CbxItem cbxItem;
	
	public CbxItemObservable(String i, String n) {
		this.cbxItem = new CbxItem(i, n);
		bind(this.cbxItem.idProperty(), this.cbxItem.nameProperty());
	}
	
	@Override
	protected CbxItem computeValue() {
		return cbxItem;
	}

	@Override
	public String toString(){
		return cbxItem.idProperty().get() +" "+ cbxItem.nameProperty().get();
	}
}
