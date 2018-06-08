
module.exports.NotificationPhone = function NotificationPhone(fromNumber, toNumber, text ){

var authSID = 'AC784a89fba3d6464adda505e3c01b52eb';
var authTOKEN = 'd424cf559cb3153a8bc31925cf438863';
var client = require('twilio')(authSID, authTOKEN);

	client.sendMessage({
		to: toNumber,
		from: fromNumber,
		body: text,
	}, function(err, data){
		if(err)
			console.log(err);
	});

}

