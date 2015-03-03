import javax.swing.*

class ValueView extends JTextField implements Observer {
    private Model model
    private currency

    public void setModel(Model model) {
        this.model?.removeObserver(this)
        this.model = model
        model.addObserver(this)
    }

    public void update(Observable o, Object currency) {
        if (currency == null || this.currency == currency)
            text = String.format("%15.2f", model.getValue(this.currency));
    }
}
