var snmp = require ("net-snmp");

var session = snmp.createSession ("10.2.19.147", "public");

var oids = ["1.3.6.1.2.1.1.5.0", // Nombre del Sistema
                    "1.3.6.1.2.1.1.3.0", //Tiempo prendido
                    "1.3.6.1.2.1.2.2.1.6.6", //MAC dispositivo
                    "1.3.6.1.4.1.41112.1.4.5.1.7.1", //Intensidad
                    "1.3.6.1.4.1.41112.1.4.5.1.9.1", //TX
                    "1.3.6.1.4.1.41112.1.4.5.1.10.1", //RX
                    "1.3.6.1.4.1.41112.1.4.5.1.5.1"]; //Señal

session.get (oids, function (error, varbinds) {
    if (error) {
        console.error (error);
    } else {
        for (var i = 0; i < varbinds.length; i++)
            if (snmp.isVarbindError (varbinds[i]))
                console.error (snmp.varbindError (varbinds[i]))
            else
            {
                console.log(error)
                console.log (varbinds[i].oid + " = " + varbinds[i].value);
            }
    }
});

session.trap (snmp.TrapType.LinkDown, function (error) {
    if (error)
        console.error (error);
});