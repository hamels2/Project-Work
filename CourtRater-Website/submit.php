<!doctype html>
<html>
<meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- viewprt for mobile -->
  <head>
    <title>submit</title> <!--title of html doc -->
    <link href="submit.css" rel="stylesheet"/><!-- submit stylesheet used -->
  </head>
  <style>
  .error {color: #FF0000; font-weight: normal;}

  </style>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

  <?php
    session_start();
    $name=$lat=$lon=$rating=$comm="";
    $nameErr=$latErr=$lonErr=$rateErr=$imagErr="";
    $good=False;
    //form validation
    if($_SERVER["REQUEST_METHOD"]=="POST"){ //if we are recieving a post
      $good=True; //we're good to go until proven otherwise
      if(!empty($_POST['comments'])){ //save comment
        $comm=$_POST['comments']; //comments not necessary
      }
      if(empty($_SESSION['email'])){ // checked if logged in
        $nameErr = "*Only Logged in users can submit";
        $good=False;
      }
      if (empty($_POST["name"])) { //check for name
        $nameErr = "*Name is required";
        $good=False;
      }
      else{
        $name = $_POST["name"];
      }


      if(empty($_POST["latitude"])){ // check for latitude
        $latErr="*Latitude is required";
        $good=False;
      }
      //check that latitude is valid
      else if(floatval($_POST["latitude"])<-90 || floatval($_POST["latitude"])>90){
        $latErr="*Latitude must be between -90 and 90 degrees";
        $good=False;
      }
      else{
        $lat=floatval($_POST["latitude"]);
      }
      if(empty($_POST["longitude"])){ //check for longitude
        $lonErr="*Longitude is required";
        $good=False;
      }
      //check that longitude is valid
      else if(floatval($_POST["latitude"])<-180 || floatval($_POST["latitude"])>180){
        $latErr="*Longitude must be between -180 and 180 degrees";
        $good=False;
      }
      else{
        $lon=floatval($_POST["longitude"]);
      }

      if(!empty($_POST['rate'])){ //check for rating
        $rating = (int)$_POST['rate'];

      }
      else{
        $rateErr="*Please rate the court";
        $good=False;
      }

    }
    if(isset($_GET['name'])){ //see if instead we came from a get
      //this data comes from review submission in courts.php
      $name=$_GET['name'];
      $lat=$_GET['lat'];
      $lon=$_GET['lon'];
    }

    function validate($input){ //validate input
      $data = trim($input);
      $data = htmlspecialchars($data);
      return $data;
    }

    if($good==True){
      $image="";
      $image = upload();
    }


    if($good==True){ //if form is good to go
      // first check if the court you want to submit already exists
      $pdo = new PDO('mysql:host=localhost;dbname=courtside','root','4WW3');
      $stmt = $pdo->prepare('SELECT idcourts FROM `courts` WHERE `name` = :name AND `latitude` = :latitude AND `longitude` = :longitude');
      $stmt->bindValue(':name',$name);
      $stmt->bindValue(':latitude',$lat);
      $stmt->bindValue(':longitude',$lon);
      $stmt->execute();
      $check = $stmt->fetchALL(PDO::FETCH_ASSOC);
      if(count($check)==0){ // if the court does not exist
        $stmt = $pdo->prepare('INSERT INTO courts(name,rating,latitude,longitude, image_url)
        VALUES (:name,:rating,:latitude,:longitude, :image)'); //create a court into db
        $stmt->bindValue(':name',$name);
        $stmt->bindValue(':rating',$rating);
        $stmt->bindValue(':latitude',$lat);
        $stmt->bindValue(':longitude',$lon);
        $stmt->bindValue(':image',$image);
        $stmt->execute();
        //then we create a corrosponding review for that court
        //get the newly created courtid
        $stmt = $pdo->prepare('SELECT idcourts FROM `courts` WHERE `name` = :name AND `latitude` = :latitude AND `longitude` = :longitude');
        $stmt->bindValue(':name',$name);
        $stmt->bindValue(':latitude',$lat);
        $stmt->bindValue(':longitude',$lon);
        $stmt->execute();
        $court= $stmt->fetch(PDO::FETCH_ASSOC);
        //use that courtid to submit the review
        $stmt = $pdo->prepare('INSERT INTO reviews(review,rating,idcourts,idusers)
        VALUES (:review,:rating,:court,:user)');
        $stmt->bindValue(':review',$comm);
        $stmt->bindValue(':rating',$rating);
        $stmt->bindValue(':user',$_SESSION['email']); //userid
        $stmt->bindValue(':court',$court['idcourts']); // courtid from previous query
        $stmt->execute();
        header('Location: courts.php?id='.$court['idcourts'].''); //create the court page
      }
      else{ //if the court does exist just create the review
        $stmt = $pdo->prepare('INSERT INTO reviews(review,rating,idcourts,idusers)
        VALUES (:review,:rating,:court,:user)');
        $stmt->bindValue(':review',$comm);
        $stmt->bindValue(':rating',$rating);
        $stmt->bindValue(':user',$_SESSION['email']);
        $stmt->bindValue(':court',$check[0]['idcourts']); //use the existing courtid
        $stmt->execute();
        header('Location: courts.php?id='.$check[0]['idcourts'].'');
      }

    }


    function upload(){
      $target_dir = "";
      $target_file = $target_dir . basename($_FILES["picture"]["name"]);
      $uploadOk = 1;
      $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
      // Check if image file is a actual image or fake image
      if(isset($_POST["submit"])) {
        $valid = getimagesize($_FILES["picture"]["tmp_name"]);
        if($valid !== False) {
            $uploadOk = 1;
        } else {
            $imagErr = "*File is not an image.";
            $uploadOk = 0;
            $good = False;
        }
      }

      // Check file size
      if ($_FILES["picture"]["size"] > 500000) {
        $imageErr = "*Sorry, your file is too large.";
        $uploadOk = 0;
        $good = False;
      }
      // Allow certain file formats
      if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
      && $imageFileType != "gif" ) {
        $imageErr= "Sorry, only JPG, JPEG, PNG & GIF files are allowed.";
        $uploadOk = 0;
        $good = False;
      }
      // Check if $uploadOk is set to 0 by an error
      if ($uploadOk == 0) {
        $imageErr = "*Sorry, your file was not uploaded.";
        $good = False;
      // if everything is ok, try to upload file
      } else {
        if (move_uploaded_file($_FILES["picture"]["tmp_name"], $target_file)) {
          return  $target_dir . basename($_FILES["picture"]["name"]);
        }
        else {
          $good = False;
          $imageErr = "Sorry, there was an error uploading your file.";
        }
      }

    }
  ?>



  <body>
    <header>
      <nav> <!--navigation bar-->
        <a href="search.php">Search</a>
        <a href="#">profile</a> <!--not yet implemented so just reloads current page-->
        <a href="#">near you</a>
        <a href="#">logout</a>
      </nav>
    </header>

    <div>
      <div class="col">
        <h1>Submission page</h1>
        <p>Park Name:</p><br/>
        <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>" enctype="multipart/form-data"> <!--form sends to courts-->
          <input type="text" name="name" value="<?php echo $name;?>"  > <!--textbox to input name of park-->
          <br/>
          <span class="error"><?php echo $nameErr;?></span>
          <br/>
          <p>Latitude and Longitude:</p><br/>
          <input type="text" name="latitude" value="<?php echo $lat;?>" ><!--textbox to input location -->
          <input type="text" name="longitude" value="<?php echo $lon;?>" >
          <br/>
          <span class="error"><?php echo $latErr;?></span>
          <span class="error"><?php echo $lonErr;?></span>
          <br/>
          <br/>
          <input type="file" name="picture" accept="image/*" ></input><!--upload image input-->
          <span class="error"><?php echo $imagErr;?></span>
          <br/>
          <br/>
          <p>Comments:</p><br/>
          <textarea  rows="4" cols="50" name="comments" ><?php echo htmlspecialchars($comm);?></textarea> <!--textarea for comments -->
          <br/>
          <br/>
          <p>Rating:</p><br/>
          <button type="button" class="one" name="one" onclick="button(1)"></button> <!--buttons for rating the park-->
          <button type="button" class="two" name="two" onclick="button(2)" ></button>
          <button type="button" class="three" name="three" onclick="button(3)"></button>
          <button type="button" class="four" name="four" onclick="button(4)"></button>
          <button type="button" class="five" name="five" onclick="button(5)"></button>
          <br/>
          <span class="error"><?php echo $rateErr;?></span>
          <br/>
          <br/>
          <input class="sub" type="submit" name="submit" ></input>
          <br/>
          <input id="rate" type="hidden" name="rate"></input>
        </form>
        <script src="Map.js"></script>
        <br/>
        <br/>
        <br/>
        <footer class="foot">
          <p>Posted by: Sam Hamel</p>
          <p>Contact information: <a href="hamels2@mcmaster.ca">hamels2@mcmaster.ca</a>.</p>
        </footer>
      </div>
    </div>
  </body>

</html>
