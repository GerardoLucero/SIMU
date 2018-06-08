
module.exports.NotificationApp = function NotificationApp(Firebase, token, url, titulo, texto){


    //creación de notificación
    var payload = {
      notification: {
        title: titulo,
        body: texto
      }
    };

    // Opciones con prioridad alta y exiración de 24hrs
    var options = {
      priority: "high",
      show_in_foreground: "True",
      timeToLive: 60 * 60 * 24
    };
    
    //Envio de mensage para dispositivos con token registrado
    //¿como consigo el token?... android? ckDAJrVTwtc:APA91bGscZo993DsFbDoKxxAVdiP0UCDJEow-Pc8uhNzxHRQTPQWGIb-j0yWoHBX0CHa_j4uLU2bXhFn4-pXAXIeUOJANiYOy_Jpzewb1-i98SBCoKL9S-Yt0XFSVhoGCxHaOOkqeNkC
    var registrationToken = token;
    Firebase.messaging().sendToDevice(registrationToken, payload, options)
      .then(function(response) {
        console.log("Successfully sent message:", response);
      })
      .catch(function(error) {
        console.log("Error sending message:", error);
      });


}
