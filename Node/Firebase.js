module.exports.Firebase = function Firebase(url){

	var serviceAccount = require('./SIMU-d4ecd8e6ad4d.json');
	var Firebase = require('firebase-admin');

	Firebase.initializeApp({
	      credential: Firebase.credential.cert(serviceAccount),
	      databaseURL: url
	    });

	var db = Firebase;

	return db;
}