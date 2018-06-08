module.exports.ReadFirebase = function ReadFirebase(db, path, fn){



	var path = db.database().ref(path);
	var radios = [];
	
	path.once("value", function(snapshot) {
	      console.log(snapshot.val());
	      radios = [];
	      radiosCopy = [];
	      snapshot.forEach(function(snapshot) {
		  	var radio = snapshot.val();
		   	//console.log(snapshot.key);
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
			fn(radios); 
	 	}, function (errorObject) {
	      console.log("The read failed: " + errorObject.code);
	});
}



