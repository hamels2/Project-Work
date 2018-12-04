<?php session_start();?>
<!doctype html>
<html>
<meta name="viewport" content="width=device-width, initial-scale=1.0"><!-- viewprt for mobile -->


  <head>
    <title>results</title> <!--title of html doc -->
    <link href="results.css" rel="stylesheet"/> <!-- results stylesheet used -->
  </head>
  <script src="Map.js"></script>

  <body>
    <header>
      <nav> <!--navigation bar-->
        <a class="link" href="search.php">Search</a>
        <a class="link" href="#">profile</a>  <!--not yet implemented so just reloads current page-->
        <a class="link" href="submit.php">submit</a>
        <a class="link" href="#">logout</a>
      </nav>
    </header>


    <div>
      <h1>Results:</h1>
      <?php
        $emailErr=$passwordErr=$password=$email="";
        if($_SESSION["lat"]!=""){ //if we're using near me
          $result = $_SESSION["query"]; //get the search info
          //get the distance from near me point to the courts
          $curLat=deg2rad(floatval($_SESSION['lat']));
          $curLong=deg2rad(floatval($_SESSION['lon']));
          $near= array();
          $i = 0;
          foreach ($result as $loc) {
            $lat = deg2rad($loc['latitude']);
            $long = deg2rad($loc['longitude']);
            $alph=deg2rad(floatval($_SESSION['lat'])-$loc['latitude']);
            $beta=deg2rad(floatval($_SESSION['lon'])-$loc['longitude']);
            $a = sin($alph/2)*sin($alph/2)+cos($lat)*cos(floatval($_SESSION['lat']))*sin($beta/2)*sin($beta/2);
            $c = 2*atan2(sqrt($a),sqrt(1-$a));
            $d = 6371000*$c;
            if($d<25){ // if the distance is less than 25km
              $near[$i]= $loc;
              $i++;
            }
            $json = json_encode($near); //send the nearby courts to javascript for map
            echo "<input id= query type= hidden  name=query value=".$json."></input>";
            createTable($near); //create the table
          }
        }

        else{ // if we are not using the near me function
          $result = $_SESSION['query'];
          $json = json_encode($result); //send the search info to javascript
          echo "<input id= query type= hidden  name=query value=".$json."></input>";
          createTable($result); //create table
        }

        function createTable($results){
          echo '<table>';
          echo '<tr>';
          echo '<th>Name</th>';
          echo '<th>Rating</th>';
          echo '<th>Location</th>';
          echo '</tr>';
          foreach($results as $court){
            echo '<tr>';
            echo "<td><a href= courts.php?id=".$court['idcourts'].">".$court['name']."</a></td>"; //use courtid to create courts page
            echo '<td>';
            for($i=0; $i<$court['rating']; $i++){ //output rating
              echo '<img class="ball" src="aba.png" alt="ball">';
            }
            echo '</td>';
            echo "<td>" .$court['latitude'].",".$court['longitude']."</td>"; //location
            echo '</tr>';
          }
          echo '</table>';
        }
      ?>

    <div>
    <br/>
    <br/>
    <div class="image" id="map"></div>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCiCJemc_5vO7cLuaTFrdVdYSHnjihCU0A&callback=initMap">
    </script>
  </body>
  <footer>
    <p>Posted by: Sam Hamel</p>
    <p>Contact information: <a href="hamels2@mcmaster.ca">hamels2@mcmaster.ca</a>.</p>
  </footer>
</html>
