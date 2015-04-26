def pl = new ProcessList()
def ps = pl.getCurrent()

println ps.size()
ps.each { p ->
	println p
}

println pl.findCommand(ps, "apache")
println pl.findCommand(ps, "apache3")
