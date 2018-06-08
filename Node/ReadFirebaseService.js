module.exports.ReadFirebaseService = function ReadFirebaseService(db, path, fn){


	var path = db.database().ref(path);
	
	path.once("value", function(snapshot) {
	      //console.log(snapshot.val());
	      Service = [];
	      Service = snapshot.val().activado;
		  fn(Service); 
	 	
	}, function (errorObject) {
	      console.log("The read failed: " + errorObject.code)});
}

