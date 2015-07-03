// https://core.telegram.org/bots/api#setwebhook
//
//Notes
//1. You will not be able to receive updates using getUpdates for as long as an outgoing webhook is set up.
//2. We currently do not support self-signed certificates.
//3. Ports currently supported for Webhooks: 443, 80, 88, 8443.

def updateId = request.getParameter('update_id')
def message = request.getParameter('message')
println 'updateId=' + updateId
println 'message=' + message
System.out.println('updateId=' + updateId)
System.out.println('message=' + message)
