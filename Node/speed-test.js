module.exports.speedTest = function speedTest(fn){

	var speedTest=require('speedtest-net');
	var ProgressBar = require('progress');
	var chalk = require("chalk");

	var test=speedTest({maxTime:5000});

	var bar,pingTime;
    var st = [];
	test.on('testserver',function(server) {
		pingTime = server.bestPing;
	});


	test.on('data',function(data){
		console.log(chalk.cyan("Ping : "),Math.abs(pingTime),chalk.dim('ms'));
		console.log(chalk.cyan("Download Speed : ") + data.speeds.download + chalk.dim(" Mbps"));
	    console.log(chalk.cyan("Upload Speed : ") + data.speeds.upload + chalk.dim(" mbps"));

	    var d = new Date();
        var datestring = d.getDate()  + "-" + (d.getMonth()+1) + "-" + d.getFullYear() + "-" + d.getHours() + "-" + d.getMinutes() + "-" + d.getSeconds();
	    st.push({fecha: datestring, ping:pingTime, subida:data.speeds.download , bajada:data.speeds.upload});

	    fn(st);
	});

	test.on('error',function(error){
		process.exit(1);
	});

}



