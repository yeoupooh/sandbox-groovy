import javax.swing.*

class RateView extends JTextField implements Observer {
    private Model model;
    private currency;

    public void setModel(Model model) {
        this.model?.removeObserver(this)
        this.model = model
        model.addObserver(this)
    }

    public void update(Observable o, Object currency) {
        if (this.currency == currency)
            text = String.format("%15.2f", model.rates[currency])
    }
}
