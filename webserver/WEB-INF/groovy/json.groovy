response.contentType = 'application/json'

out << new File("static/files/" + request.getParameter("name") + ".json").text
