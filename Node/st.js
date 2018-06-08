const speedTest = require('speedtest-net');
var fs = require("fs");
const test = speedTest({maxTime: 5000});
var st = [];
var Job = require('cron').CronJob;


//new Job('*/5 * * * * *', function() {
	console.log("1");
	test.on('testserver',function(server) {
			pingTime = server.bestPing;
	});

	test.on('data', data => {
	  console.log(data);
	  console.log("2");
	  var timeStamp = Math.floor(Date.now());
	  console.log(timeStamp);


	  fs.readFile('./test.txt', function read(err, cont) {

	  		if (cont == null) {
	  			var cont = "";
	  		}
		    fs.writeFile("./test.txt", 
		  	cont + "{fecha:" + new Date().toString() +", ping:" +pingTime +", subida:"+data.speeds.download +", bajada:"+data.speeds.upload +", servidor:"+ data.server.host +"} \r\n"
		  	, function (err) {
			    // la funcion es la que maneja lo que sucede despues de termine el evento
			    if (err) {
			        return console.log(err);
			    }
			    console.log("The file was saved!");
			});
		});
		

	});


	test.on('error', err => {
	  console.error(err);
	});

//}).start();