module.exports.ReadFirebaseUsr = function ReadFirebaseUsr(db, path, fn){


	var path = db.database().ref(path);
	var Users = [];
	
	path.once("value", function(snapshot) {

	      snapshot.forEach(function(snapshot) {
		  	var user = snapshot.val();
		   	//console.log(snapshot.key);
		   	//console.log(radio.ipAddres);
		   	//console.log(radio.estate);
		   	Users.push({email:user.email, name:user.name, token: user.token, enableEmail: user.enableEmail, enablePush: user.enablePush});

	  	  });
			fn(Users);
	 	}, function (errorObject) {
	      console.log("The read failed: " + errorObject.code);
	});
}



