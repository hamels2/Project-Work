
function setLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition);
  }
  else {
    alert("geolocation not supported by browser");
  }
   window.location.assign("results.php");
}


function showPosition(position){
  var lat = position.coords.latitude;
  var long = position.coords.longitude;
  document.getElementById('lat').value=lat;
  document.getElementById('lon').value=long;
  localStorage.setItem("lat",lat);
  localStorage.setItem("long",long);

}

function initMap(){
  var query = document.getElementById('query').value; //get search info
  var courts = JSON.parse(query);
  var loc;
  loc = {lat: Number(courts[0].latitude), lng: Number(courts[0].longitude)}; //get the location

  var map = new google.maps.Map(document.getElementById('map'), { //draw map
    zoom: 12,
    center: loc
  });

  for(i=0; i<courts.length; i++){ //place down markers
    loc = {lat: Number(courts[i].latitude), lng: Number(courts[i].longitude)};
    var marker = new google.maps.Marker({
      position: loc,
      map: map,
      title: courts[i].name
    });

    var contentString = '<div id="content">'+
      '<div id="siteNotice">'+
      '</div>'+
      '<h1 id="firstHeading" class="firstHeading">'+courts[i].name+'</h1>'+
      '<div id="bodyContent">'+
      '<p>Latitude: '+ courts[i].latitude+' Longitude: '+courts[i].longitude+ '</p>'+
      '<p>Link: <a href="courts.php?id="'+courts[i].idcourts+'>'+
      courts[i].name+'</a> '+
      '</p>'+
      '</div>'+
      '</div>';

    var infowindow = new google.maps.InfoWindow({
      content: contentString
    });
    marker.addListener('click', function() {
      infowindow.open(map, marker);
    });
  }


}



function button(num){
  document.getElementById('rate').value =num;
}
