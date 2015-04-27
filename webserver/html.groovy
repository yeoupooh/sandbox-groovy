html.html {
    header {
        link (href:"//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css",rel:"stylesheet")
        script (src:"https://code.jquery.com/jquery-2.1.3.min.js")
        script (src:"//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js")
        
        script (type:"text/javascript", '''
            $(document).ready(function(){
            
            console.log('ready');
            
                $("#alert").click(function(){
                alert('test');
                });
            });
        ''') // javascript
    } // header
    
    body {
        button(id:"alert", type:"button", class:"btn btn-primary", "alert")
        
        table (class:"table") {
            thead {
                tr (class:"info") {
                    td ("head")
                    td ("head")
                }
            }
            for (def i = 0; i < 10; i++) {
                tr (class:i % 3 == 0 ? "danger" : "success") {
                    td ("hello" + i)
                    td ("hello" + i)
                }
            }
        } // table
    } // body
}
