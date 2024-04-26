
function aggiornaSideBar(torneiIscritti, torneiDisponibili) {
		var divIscritti = document.getElementById("sidebarIscritti");
		var divDisponibili = document.getElementById("sidebarDisponibili");
		
		divIscritti.innerHTML="";
		divDisponibili.innerHTML="";
		//inizia a riempire i 2 div
}


function getEventiPerData(sessionID){

	var dati = {
		"sessionId" : sessionID,
		"type_req": "d",   // d = mando i dati per quella data e in risposta ottengo tutti gli eventi
		"data" : "2024-10-12"
	}



	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		// Typical action to be performed when the document is ready:
		var risposta = xhttp.responseText;  



			console.log(JSON.parse(risposta));
		}
	};
	xhttp.open("POST", getTornei, true);
	xhttp.send(JSON.stringify(dati));


}

function getMeseEventi(sessionID){

	var dati = {
		"sessionId" : sessionID,
		"type_req": "m",   // m = mando i dati per quel mese
		"mese" : "10",
		"anno":"2024"
	}



	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		// Typical action to be performed when the document is ready:
		var risposta = xhttp.responseText;  



			console.log(JSON.parse(risposta));
		}
	};
	xhttp.open("POST", getTornei, true);
	xhttp.send(JSON.stringify(dati));



}



function abbandonaEvento(sessionID){

	
		var dati = {
			"sessionId" : sessionID,
			"type_req": "d",
			"data" : "2024-10-12",
			"nome_evento" : "aldini",
			"orario" : "21:25"
		}
	
	
	
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			// Typical action to be performed when the document is ready:
			var risposta = xhttp.responseText;  // sara true (evento eliminato) o (errori relativi all'eliminazione dell'evento)
	
			if(risposta == "true"){
				
				//richiama il metodo che costruisce sidebar
	
				
			}
	
	
				console.log(risposta);
			}
		};
		xhttp.open("POST", partecipazioniServlet, true);
		xhttp.send(JSON.stringify(dati));
}
	



function iscriviEvento(sessionID){
	
	var dati = {
        "sessionId" : sessionID,
        "type_req": "i",
        "data" : "2024-10-12",
        "nome_evento" : "cricketTorneo",
        "orario" : "21:25"
    }



    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
        // Typical action to be performed when the document is ready:
        var risposta = xhttp.responseText;  // sara true (evento eliminato) o (errori relativi all'eliminazione dell'evento)

        if(risposta == "true"){
            
            //richiama il metodo che costruisce sidebar

            
        }


            console.log(risposta);
        }
    };
    xhttp.open("POST", partecipazioniServlet, true);
    xhttp.send(JSON.stringify(dati));
}

function deleteEvento(sessionID){

    var dati = {
        "sessionId" : sessionID,
        "type_req": "d",
        "data" : "2024-10-12",
        "nome_evento" : "aldini",
        "orario" : "21:25"
    }



    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
        // Typical action to be performed when the document is ready:
        var risposta = xhttp.responseText;  // sara true (evento eliminato) o (errori relativi all'eliminazione dell'evento)

        if(risposta == "true"){
            
            //richiama il metodo che costruisce sidebar

            
        }


            console.log(risposta);
        }
    };
    xhttp.open("POST", torneiManagerServlet, true);
    xhttp.send(JSON.stringify(dati));
}


function updateEvento(sessionID){

    var dati = {
        "sessionId" : sessionID,
        "type_req": "u",
        "data" : "2024-10-11",
        "nome_evento" : "amico",
        "descrizione" : "ho fato una modifica",  //luogo evento o un sport non in elenco
        "orario" : "21:25",
        "numero_partecipanti_max" : "10",
        "eta_minima":"6",
        "sport":"Calcio",
    }



    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
        // Typical action to be performed when the document is ready:
        var risposta = xhttp.responseText;  // sara true (evento modificato) o (errori relativi alla modifica dell'evento)

        if(risposta == "true"){
            
            //richiama il metodo che costruisce sidebar

            
        }


            console.log(risposta);
        }
    };
    xhttp.open("POST", torneiManagerServlet, true);
    xhttp.send(JSON.stringify(dati));
}

function getEventoData(sessionID) { // sostituito con createEvento
		//calcolo data
		//costruzione json da mandare al backend

        
		var dati = {
				"sessionId" : sessionID,
                "type_req": "c",
				"data" : "2024-10-19",
				"nome_evento" : "keema",
				"descrizione" : "ciao sto creando un evento ",  //luogo evento o un sport non in elenco
				"orario" : "23:25",
				"numero_partecipanti_max" : "4",
                "eta_minima":"6",
                "sport":"Hockey",
                "tipo_evento" : "Crea esterno"
		}

        
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			   // Typical action to be performed when the document is ready:
			   var risposta = xhttp.responseText;  // sara true (evento creato) o (errori relativi alla creazione dell'evento)

               if(risposta == "true"){
                
                 //richiama il metodo che costruisce sidebar

                
               }


                console.log(risposta);
			}
		};
		xhttp.open("POST", torneiManagerServlet, true);
		xhttp.send(JSON.stringify(dati));
	}


function getMese() {
		//calcolo data
		//costruzione json da mandare al backend
		var dati = {
				"sessionId" : sessionID,
				"mese_corrente" : mese_corrente
		}
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			   // Typical action to be performed when the document is ready:
			   var risposta = JSON.parse(xhttp.responseText);
			   var torneiIscritti = risposta["iscritti"];
			   var torneiDisponibili = riposta["disponibili"];
			   aggiornaSideBar(torneiIscritti, torneiDisponibili);
			}
		};
		xhttp.open("POST", "link backend", true);
		xhttp.send(JSON.stringify(dati));
	}
	
	function getGiorno() {
		//calcolo data
		
		date = retriveDate();
		
		//costruzione json da mandare al backend
		var dati = {
				"sessionId" : sessionID,
				"data" : date //la data deve essere nel formato yyyy/mm/dd
		}
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			   // Typical action to be performed when the document is ready:
			   var risposta = JSON.parse(xhttp.responseText);
			   var torneiIscritti = risposta["iscritti"];
			   var torneiDisponibili = riposta["disponibili"];
			   aggiornaSideBar(torneiIscritti, torneiDisponibili);
			}
		};
		xhttp.open("POST", "link backend", true);
		xhttp.send(JSON.stringify(dati));
	}
	
	function createEvento() {
		//calcolo data
		//prelievo dei dati del form crea evento
	//var nome_evento = document.getElementById("nome_evento").value;
		
		//costruzione json da mandare al backend
		var dati = {
				"sessionId" : sessionID,
				"data" : date, //la data deve essere nel formato yyyy/mm/dd
				"nome_evento" : nome_evento,
				"luogo_evento" : luogo_evento,
				"orario" : orario_evento,
				"numero_partecipanti_max" : numero_partecianti_max
				
		}
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			   // Typical action to be performed when the document is ready:
			   var risposta = JSON.parse(xhttp.responseText);
			   var torneiIscritti = risposta["iscritti"];
			   var torneiDisponibili = riposta["disponibili"];
			   aggiornaSideBar(torneiIscritti, torneiDisponibili);
			}
		};
		xhttp.open("POST", "link backend", true);
		xhttp.send(JSON.stringify(dati));
	}
	
	
	
		function iscrizioneEvento() {
		//calcolo data
		//costruzione json da mandare al backend
		var dati = {
				"sessionId" : sessionID,
				"utenteID" : utenteID
				
		}
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			   // Typical action to be performed when the document is ready:
			   var risposta = JSON.parse(xhttp.responseText);
			   var torneiIscritti = risposta["iscritti"];
			   var torneiDisponibili = riposta["disponibili"];
			   aggiornaSideBar(torneiIscritti, torneiDisponibili);
			}
		};
		xhttp.open("POST", "link backend", true);
		xhttp.send(JSON.stringify(dati));
	}
	
	function getDatiUtente() {
		//calcolo data
		//costruzione json da mandare al backend
		var dati = {
				"sessionId" : sessionID,
				"utenteID" : utenteID
				
		}
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			   // Typical action to be performed when the document is ready:
			   var risposta = JSON.parse(xhttp.responseText);
			   var torneiIscritti = risposta["iscritti"];
			   var torneiDisponibili = riposta["disponibili"];
			   aggiornaSideBar(torneiIscritti, torneiDisponibili);
			}
		};
		xhttp.open("POST", "link backend", true);
		xhttp.send(JSON.stringify(dati));
	}
