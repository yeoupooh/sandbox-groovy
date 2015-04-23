response.contentType = 'application/json'
json.hello {
    "name" "me"
    "age" 100
    array (1,2,3,4)
    objarray ({ "a" "b" }, { "a" "c" })
}
out << json
