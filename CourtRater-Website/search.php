<?php session_start();?>
<!doctype html>
<html>
<meta name="viewport" content="width=device-width, initial-scale=1.0"><!-- viewprt for mobile -->
  <head>
    <title>search</title><!--title of html doc -->
    <link href="search.css" rel="stylesheet"/><!-- search stylesheet used -->
  </head>


  <?php
    $search=$rating="";

    if($_SERVER["REQUEST_METHOD"]=="POST"){ //check form
      $search = $_POST['search'];
      $rating=(int)$_POST['stars'];
      $chk = new PDO('mysql:host=localhost;dbname=courtside','root','4WW3');
      if($search!=""){ //if name was not left blank search based on rating and name
        $stmt=$chk->prepare('SELECT name,idcourts,rating,latitude,longitude FROM `courts` WHERE `name` LIKE :search AND `rating` >= :rating');
        $stmt->bindValue(':search',$search);
        $stmt->bindValue(':search',$rating);
      }
      else{ //if name was left blank search just based on rating
        $stmt=$chk->prepare('SELECT name,idcourts,rating,latitude,longitude FROM `courts` WHERE `rating` >= :rating');
        $stmt->bindValue(':rating',$rating);
      }
      $stmt->execute();
      $result = $stmt->fetchAll(PDO::FETCH_ASSOC); //get the results
      $_SESSION['query']=$result; //save the query
      if($_POST['lat']!=""){ //if near me was chosen save location
        $_SESSION['lat']=$_POST['lat'];
        $_SESSION['lon']=$_POST['lon'];
      }
      header('Location: results.php');
      exit();
    }
  ?>


  <body>

    <header>
      <nav><!--navigation bar-->
        <a href="submit.php">submit</a>
        <a href="#">profile</a> <!--not yet implemented so just reloads current page-->
        <a href="#">near you</a>
        <a href="#">logout</a>
      <nav/>
    </header>
    <div>
      <h1 class="ser">Search</h1>
      <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> <!--form sends to results-->
        <p>Name of Park:</p><br/>
        <input type="text" name="search" value=""> <!--search box-->
        <br/>
        <br/>
        <p>Rating:</p>
        <select name="stars"> <!--ratings to search for-->
          <option value="1"selected>All</option>
          <option value="2">2 stars and above</option>
          <option value="3">3 stars and above</option>
          <option value="4">4 stars and above</option>
          <option value="5">5 stars</option>
        </select>
        <br/>
        <input type="submit" value = "Search">
        <br/>
        <br/>
        <p>or:</p>
        <br/>
        <input id="lat" type="hidden" name="lat"></input>
        <input id="lon" type="hidden" name="lon"></input>
        <button id="location" type="button" onclick="setLocation()">near me</button>
      </form>
    </div>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCiCJemc_5vO7cLuaTFrdVdYSHnjihCU0A&callback=initMap">
    </script>
    <script src="Map.js"></script>
  </body>
  <footer>
    <p>Posted by: Sam Hamel</p>
    <p>Contact information: <a href="hamels2@mcmaster.ca">hamels2@mcmaster.ca</a>.</p>
  </footer>
</html>
