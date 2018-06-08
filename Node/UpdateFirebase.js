function UpdateFirebase(url, path){
function ReadFirebase(url, path){
    var serviceAccount = require("./SNMP-298342681740.json");
	var Firebase = require("firebase-admin");


	Firebase.initializeApp({
	      credential: Firebase.credential.cert(serviceAccount),
	      databaseURL: url
	});

}

