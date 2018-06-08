
module.exports.SendNotificationEmail = function SendNotificationEmail(namemail, tipoEmail, fromemail, frompass, toemmail, Subject, HTML){

var nodemailer = require("nodemailer");

	var smtpTransport = nodemailer.createTransport({
	   service: tipoEmail,  // sets automatically host, port and connection security settings
	   auth: {
	       user: fromemail,
	       pass: frompass
	   }
	});

	var emailoptions = {  
	   from: namemail+ " <"+fromemail+">", 
	   to: "<"+toemmail+">", 
	   subject: Subject, // subject // body
	   html: HTML
	};

	smtpTransport.sendMail(emailoptions, function(error, response){  //callback
	   if(error){
	       console.log(error);
	   }else{
	       console.log("Message sent: " + response.message);
	   }
	   smtpTransport.close(); 
	});
}
