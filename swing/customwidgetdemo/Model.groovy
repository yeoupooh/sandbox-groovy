class Model extends Observable {
    static CURRENCY = ["USD", "EURO", "YEN"]

    private Map rates = new HashMap()
    private long value

    void initialize(initialRates) {
        (0..CURRENCY.size() - 1).each {
            setRate(CURRENCY[it], initialRates[it])
        }
    }

    // setting rate for currency
    void setRate(currency, f) {
        rates.put(currency, f);
        setChanged();
        notifyObservers(currency);
    }

    // setting new value for currency
    void setValue(currency, double newValue) {
        value = Math.round(newValue / rates[currency]);
        setChanged();
        notifyObservers(null);
    }

    // getter for value for particular currency
    def getValue(currency) {
        value * rates[currency]
    }
}
