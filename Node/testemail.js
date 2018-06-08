var nodemailer = require("nodemailer");

var smtpTransport = nodemailer.createTransport({
   service: "Gmail",  // sets automatically host, port and connection security settings
   auth: {
       user: "luceroriosg@gmail.com",
       pass: "electronica1993"
   }
});

smtpTransport.sendMail({  //email options
   from: "NASA <luceroriosg@gmail.com>", // sender address.  Must be the same as authenticated user if using GMail.
   to: "<topitzinmm@gmail.com>", // receiver
   subject: "Un asteroide ha sido detectado", // subject // body
   html: "<b>Hello, <strong>{{username}}</strong>, Your password is:\n<b>{{ password }}</b></p>"
}, function(error, response){  //callback
   if(error){
       console.log(error);
   }else{
       console.log("Message sent: " + response.message);
   }
   
   smtpTransport.close(); // shut down the connection pool, no more messages.  Comment this line out to continue sending emails.
});