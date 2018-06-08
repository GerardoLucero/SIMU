var Job = require('cron').CronJob;
RFirebase = require('./ReadFirebase.js');
Firebase = require('./Firebase.js');
Snmp = require('./RequestSnmp.js');
Check = require('./Checkradios.js');
Servicio = require('./ReadFirebaseService.js');
Speedt = require('./speed-test.js');
UFirabase = require('./UpdateFirebaseRadios.js');
Red = require('./UpdateFirebaseSpeedtest.js')

var db = Firebase.Firebase('https://simu-c185f.firebaseio.com');

new Job('59 * * * * *', function() {

  console.log(new Date().toString()); 

  Servicio.ReadFirebaseService( db, 'Sistema', function(servicio){

  		

	  	if(servicio == true){

	  		console.log('Servicio encendido');
	  		RFirebase.ReadFirebase(db, 'Ubiquiti', function(radiosDB){
		  		//console.log(radiosDB);
		  		Snmp.RequestSnmp(radiosDB, function(radiosAc) {
				console.log(radiosAc);
					Speedt.speedTest( function(red){
						console.log(red);
						Check.Checkradios(db, red, radiosAc);

						//UPDATE
						UFirabase.UpdateFirebaseRadios(db, radiosAc);
						Red.UpdateFirebaseSpeedtest(db, red);

						
					    //EVENTOS IMPORTANTES

					});	

	  			});
	  		});
  		}
	});  
}).start();