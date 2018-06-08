var snmp = require ("net-snmp");
var session = snmp.createSession ("10.2.19.147", "public");

/*var oids = [ "1.3.6.1.2.1.1.5.0", 
             "1.3.6.1.2.1.1.3.0", 
             "1.3.6.1.2.1.1.1.0", 
             "1.3.6.1.2.1.1.4.0", 
             "1.3.6.1.2.1.2.2.1.6.6", 
             "1.3.6.1.4.1.41112.1.4.5.1.2.1",
             "1.3.6.1.4.1.41112.1.4.5.1.7.1",
             "1.3.6.1.4.1.41112.1.4.5.1.8.1",
             "1.3.6.1.4.1.41112.1.4.5.1.9.1",
             "1.3.6.1.4.1.41112.1.4.5.1.10.1",
             "1.3.6.1.4.1.41112.1.4.5.1.5.1"];*/

var oids = ["1.3.6.1.2.1.1.3.0"];

try{
    session.get (oids, function (error, varbinds) {
        if (error) {
            console.log (error.message);
        } else {
            for (var i = 0; i < varbinds.length; i++)
                if (snmp.isVarbindError (varbinds[i])){
                    console.log (snmp.varbindError (varbinds[i]));
                }
                else{
                    console.log (varbinds[i].oid + " = " + varbinds[i].value);
                }
        }
    });
}
catch(err){
    console.log("catch");
}

