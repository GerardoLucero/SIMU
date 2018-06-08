
module.exports.UpdateFirebaseRadios = function UpdateFirebaseRadios(db, Copyradios){


	for (var i = Copyradios.length - 1; i >= 0; i--) {

		var ubiquitiRef = db.database().ref("Ubiquiti/"+Copyradios[i].key);
		ubiquitiRef.update({
		    estado: Copyradios[i].estado,
		    tiempo: Copyradios[i].tiempo,
		    nombre: Copyradios[i].nombre,
		    mac: Copyradios[i].mac,
		    tx: Copyradios[i].tx,
		    rx: Copyradios[i].rx,
		    signal: Copyradios[i].signal
		});
	}
}
