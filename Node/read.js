var Job = require('cron').CronJob;
var serviceAccount = require('./SIMU-d4ecd8e6ad4d.json');
var Firebase = require('firebase-admin');


Firebase.initializeApp({
      credential: Firebase.credential.cert(serviceAccount),
      databaseURL: 'https://simu-c185f.firebaseio.com'
    });
var db = Firebase.database()
var path = db.ref("Ubiquiti");
var radios = [];
var radiosCopy = [];

//SNMP
var snmp = require ("net-snmp");
var j = 0;

new Job('*/50 * * * * *', function() {
  console.log(new Date().toString()); 


	path.once("value", function(snapshot) {
	      console.log(snapshot.val());
	      radios = [];
	      radiosCopy = [];
	      snapshot.forEach(function(snapshot) {
		  	var radio = snapshot.val();
		   	console.log(snapshot.ip);
		   	//console.log(radio.ipAddres);
		   	//console.log(radio.estate);
		   	radios.push({ip:radio.ip, 
				    	estado: radio.estado, 
				    	nombre: radio.nombre, 
				    	tiempo: radio.tiempo, 
				    	mac: radio.mac,
				    	intensidad: radio.intensidad, 
				    	tx: radio.tx, 
				    	rx: radio.rx,
				    	signal: radio.signal,
				    	key: snapshot.key});

	  	  });
	      	var c = 0;
	  	  	for (a = 0 ; a < radios.length;a++ )
	  	  	{
	  	  		var session = snmp.createSession (radios[a].ip, "public");
				var oids = ["1.3.6.1.2.1.1.3.0"];
					console.log(radios[a].ip);
					session.get (oids,function (error, varbinds) {
						var pass = false;
						if (error) {
						        console.log(error.message);
						        radiosCopy.push({ip:radios[a].ip, 
					    				estado:false, 
					    				nombre: radio.nombre, 
					    				tiempo: 0, 
					    				mac: radio.mac,
					    				intensidad: 0, 
					    				tx: 0, 
					    				rx: 0,
					    				signal: 0, 
					    				key: radios[a].key});
						    } else {
						        for (var i = 0; i < varbinds.length ; i++)
						            if (snmp.isVarbindError (varbinds[i])){
						            	console.log("error");
						            radiosCopy.push({ip:radios[a].ip, 
					    				estado:false, 
					    				nombre: radios[a].nombre, 
					    				tiempo: 0, 
					    				mac: radio.mac,
					    				intensidad: 0, 
					    				tx: 0, 
					    				rx: 0,
					    				signal: 0, 
					    				key: radios[a].key});
						            }
						            else{
						                console.log (varbinds[i].oid + " = " + varbinds[i].value);
						                //radiosCopy.push({name:radios[c].name, ipAddres:radios[c].ipAddres, estate:true});
						                pass = true;
						    		}
						    		if(pass){
				    			radiosCopy.push({ip:radios[a].ip, 
				    				estado:true, 
				    				nombre: varbinds[0].value, 
				    				tiempo: varbinds[1].value, 
				    				mac: varbinds[2].value,
				    				intensidad: varbinds[3].value, 
				    				tx: varbinds[4].value, 
				    				rx: varbinds[5].value,
				    				signal: varbinds[6].value,
				    				key: radios[a].key});
				    }
						}
						c++;
						if(c == radios.length)  {
							console.log(radios);
	  	  					console.log(radiosCopy);
						}  
					});
	  	  	}
	  	  	
	}, function (errorObject) {
	      console.log("The read failed: " + errorObject.code);
	});
}).start();