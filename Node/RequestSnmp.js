module.exports.RequestSnmp = function RequestSnmp(radios, fn){

var snmp = require ("net-snmp");
var c = 0;
var radiosCopy = [];

	radios.forEach(function(radio) {


		var session = snmp.createSession (radio.ip, "public");
		var oids = ["1.3.6.1.2.1.1.5.0", // Nombre del Sistema
					"1.3.6.1.2.1.1.3.0", //Tiempo prendido
					"1.3.6.1.2.1.2.2.1.6.6", //MAC dispositivo
					"1.3.6.1.4.1.41112.1.4.5.1.7.1", //Intensidad
					"1.3.6.1.4.1.41112.1.4.5.1.9.1", //TX
					"1.3.6.1.4.1.41112.1.4.5.1.10.1", //RX
					"1.3.6.1.4.1.41112.1.4.5.1.5.1"]; //Señal
		session.get (oids,function (error, varbinds) {
			var pass = false;
			if (error) {
			    console.log(error.message);
			    console.log(radio.ip);
			    console.log(error);
				    radiosCopy.push({ip:radio.ip, 
					    				estado:false, 
					    				nombre: radio.nombre, 
					    				tiempo: 0, 
					    				mac: radio.mac,
					    				intensidad: 0, 
					    				tx: 0, 
					    				rx: 0,
					    				signal: 0, 
					    				key: radio.key});
			} else {
			    for (var i = 0; i < varbinds.length ; i++)
			        if (snmp.isVarbindError (varbinds[i])){
			          	console.log("error");
			          	console.log("2");
				    radiosCopy.push({ip:radio.ip, 
					    				estado:false, 
					    				nombre: radio.nombre, 
					    				tiempo: 0, 
					    				mac: radio.mac,
					    				intensidad: 0, 
					    				tx: 0, 
					    				rx: 0,
					    				signal: 0,
					    				key: radio.key});
			        }
			        else{
				        console.log (varbinds[i].oid + " = " + varbinds[i].value);
				        pass = true;
				    }
				    //
				    //PUSH
				    if(pass){
				    	radiosCopy.push({ip:radio.ip, 
				    				estado:true, 
				    				nombre: varbinds[0].value.toString(), 
				    				tiempo: varbinds[1].value, 
				    				mac: varbinds[2].value.toString(),
				    				intensidad: varbinds[3].value, 
				    				tx: varbinds[4].value, 
				    				rx: varbinds[5].value,
				    				signal: varbinds[6].value,
				    				key: radio.key});
				    }
				}
			c++;

			if(c == radios.length)  {
		        fn(radiosCopy);
		    }
		});

		

    });

}



