<!doctype html>
<html>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- viewprt for mobile -->
  <header>
    <nav> <!--navigation bar-->
      <a href="search.php">Search</a>
      <a href="#">profile</a> <!--not yet implemented so just reloads current page-->
      <a href="submit.php">submit</a>
      <a href="#">logout</a>
    </nav>

  </header>
  <?php
    $courtID = $_GET['id']; //get the court id from the results page or submit page
    $chk = new PDO('mysql:host=localhost;dbname=courtside','root','4WW3');
    //query for court info
    $stmt=$chk->prepare('SELECT name,rating,latitude,longitude, image_url FROM `courts` WHERE `idcourts` LIKE :courtID');
    $stmt->bindValue(':courtID',$courtID);
    $stmt->execute();
    $court = $stmt->fetchALL(PDO::FETCH_ASSOC);
    //encode the query to send to javascript which will use the location info
    $json = json_encode($court);
    echo "<input id= query type= hidden  name=query value='".($json)."'></input>"; //use hidden input to send to javascript
    //query reviews corresponding to this court
    $stmt=$chk->prepare('SELECT rating,review,idusers FROM `reviews` WHERE `idcourts` LIKE :courtID');
    $stmt->bindValue(':courtID',$courtID);
    $stmt->execute();
    $reviews = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $usernames = array();
    // query the name of users since reviews only contains their id
    foreach ($reviews as $key){
      $stmt=$chk->prepare('SELECT name FROM `users` WHERE `email` LIKE :userID');
      $userID=$key['idusers'];
      $stmt->bindValue(':userID',$userID);
      $stmt->execute();
      $name = $stmt->fetch(PDO::FETCH_ASSOC);
      $usernames[$userID]=$name['name'];
    }
    //create table
    echo '<head>';
    echo '<title>'.$court[0]['name'].'</title>';
    echo '<link href="courts.css" rel="stylesheet"/>';
    echo '</head>';
    echo '<div>';
    echo  '<div class="top">';
    echo  '<h1>Submission page</h1>';
    echo  '<h1>'.$court[0]['name'].'</h1>';
    echo    '<h3>Average Rating:</h3>';
    for($i=0; $i<$court[0]['rating']; $i++){ //output user rating
      echo '<img class="big" src="aba.png" alt="ball">';
    }
    echo '<h3> Location: '.$court[0]['latitude'].', '.$court[0]['longitude'].'</h3>'; //location
    echo '</div>';
    echo '<table>';

    echo '<tr>';
    echo '<th>User</th>';
    echo '<th width="15%">Rating</th>';
    echo '<th>Comments</th>';
    echo  '</tr>';
    foreach($reviews as $key){ //use the usernames instead of userids
      echo '<tr>';
      echo '<td>'.$usernames[$key['idusers']].'</td>';
      echo '<td>';
      for($i=0; $i<$key['rating']; $i++){
        echo '<img class="ball" src="aba.png" alt="ball">';
      }
      echo '</td>';
      echo '<td><p>'.$key['review'].'</p></td>';
      echo '</tr>';
    }
    echo '</table>';
  ?>


      <br/>
      <br/>

      <img class="court" src=<?php echo $court[0]['image_url'] ?> alt="Map"></img>
      <br/>

      <div class="image" id="map"></div><!--image of court-->
      <br/>
      <br/>
      <?php //go to page to submit review
        echo '<form method="get" action="submit.php" target="_blank"';
        echo '<br/>';
        //send over data about the court so it just makes a review and not a new court
        echo '<input type="hidden" name="name" value='.$court[0]['name'].'></input>';
        echo '<input type="hidden" name="lat" value='.$court[0]['latitude'].'></input>';
        echo '<input type="hidden" name="lon" value='.$court[0]['longitude'].'></input>';
        echo '<br/>';
        echo '<br/>';
        echo '<input class="sub" type="submit" value="submit review"></input>';
        echo '</form>';
      ?>
      <footer>
        <p>Posted by: Sam Hamel</p>
        <p>Contact information: <a href="hamels2@mcmaster.ca">hamels2@mcmaster.ca</a>.</p>
      </footer>
    </div>
    <script src="Map.js"></script>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCiCJemc_5vO7cLuaTFrdVdYSHnjihCU0A&callback=initMap">
    </script>

  </body>

</html>
