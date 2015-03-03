import org.jfree.data.time.*
import org.jfree.chart.*

/* 
   vals is a map, where:
      key is a string of the form 'yyyy-MM-dd'
      value is also a string of a decimal number, for e.g. 145.75
   name is a string naming the name of the series
*/
@Grab(group='jfree', module='jfreechart', version='1.0.12')
def createSeries(vals, name) {
    def series = new TimeSeries(name, Day.class)
    def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd")

    vals.each { dateString, valString ->
        def date = sdf.parse(dateString)
        series.add(new Day(date), Double.valueOf(valString));
    }
    series
}

/*
   series is a list of TimeSeries objects
   name is a string which can be used as the name of the chart
*/
def createChart(series, name) {
    def dataset = new TimeSeriesCollection()
    series.each { dataset.addSeries(it) }    
    ChartFactory.createTimeSeriesChart(
        name,
        "Year",                    // domain axis label
        "Close Prx",               // range axis label
        dataset,                   // data
        true,                      // create legend?
        true,                      // generate tooltips?        
        false)                     // generate URLs?
}

/*
   writes the provided chart object to a temp file and returns the file
*/
def write(chart) { 
    def file = File.createTempFile("plot", ".jpg")          // create a temporary file to hold the chart image
    ChartUtilities.saveChartAsJPEG(file, chart, 500, 400)   // the file will be 500px wide and 400px tall
    file 
}

/*
def vals = [
   ['2009-11-01': 10, '2009-11-02': 20, '2009-11-03': 50, '2009-11-04': 20],
   ['2009-11-01': 50, '2009-11-02': 20, '2009-11-03': 10, '2009-11-04': 40]
]
def series = vals.collect { createSeries(it, "some stock") }
def out = write(createChart(series, "test"))
println "Created chart at ${out.canonicalPath}"
*/

/*
   url is a string representing the web address from which to read
*/
def read(url) {
    println "Reading from url $url"
    def lines = new URL(url).text.split("\n").toList()  // split the text into lines
    println "Read ${lines.size()} lines"
    lines[1..-1]                                        // ignore the first line, which is the heading
}

/*
   lines is a list of string, each containing a line of data from Yahoo Finance API
*/
def parse(lines) {
    def vals = [:]
    lines.each { line ->
        def splits = line.split(",")
        vals[splits[0]] = splits[4]       // pick out the date and close price, which are the 
                                          // 1st and 5th value elements in a line
    }
    vals
}

/*
   symbols is a list of string, each representing a stock symbol
*/
def plot(symbols) {
    def series = symbols.collect { symbol ->    // for each symbol create a series
        def url = "http://ichart.finance.yahoo.com/table.csv?s=${symbol}"
        def vals = parse(read(url))
        createSeries(vals, symbol)
    }
    createChart(series, symbols.join(', '))     // create a chart from the serieses
}

def symbols = ["YHOO", "JAVA"]
def chart = plot(symbols)
def jpg = write(chart)
jpg.renameTo(new File("symbols.jpg"))
