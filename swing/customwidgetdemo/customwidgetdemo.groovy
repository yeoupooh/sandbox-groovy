import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

swing = new SwingBuilder()
model = new Model()

frame = swing.frame(title: "Groovy SwingBuilder MVC Demo", layout: new GridLayout(4, 3), size: [300, 150],
        defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {

    label("currency")
    label("rate")
    label("value")

    for (c in Model.CURRENCY) {
        label(c)
        widget(new RateView(), model: model, currency: c,
                action: swing.action(closure: { event ->
                    event.source.model.setRate(event.source.currency, event.source.text.toDouble());
                }))
        widget(new ValueView(), model: model, currency: c, action: swing.action(closure: { event ->
            event.source.model.setValue(event.source.currency, event.source.text.toDouble());
        }))
    }
}

frame.show()
model.initialize([1.0, 0.83, 0.56]);
