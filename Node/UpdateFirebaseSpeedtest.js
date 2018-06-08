module.exports.UpdateFirebaseSpeedtest = function UpdateFirebaseSpeedtest(db, red){


	for (var i = red.length - 1; i >= 0; i--) {

		var Ref = db.database().ref("SpeedTest");
		Ref.child(red[i].fecha).set({bajada: red[i].bajada,
									subida: red[i].subida,
		    						ping: red[i].ping});
	}
}
