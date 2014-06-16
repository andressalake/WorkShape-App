var express = require('express');
var router = express.Router();

var users = [{}]	//start at 1, for boolean sanity , array
var facebookUsers = {}  // = new Object()
var twitterUsers = {}
var googleUsers = {}

/* GET users listing. */
// router.get('/', function(req, res) {
//   res.send('respond with a resource');
// });

var updateSocialBacklinks = function (user) {
	var userID = user.userID

	var fbUID = user.fbUID
	var twitterUID = user.twitterUID
	var googleUID = user.googleUID
	
	if (fbUID) {
	log.d("users","saving user "+userID+" with Facebook token "+fbUID)
		facebookUsers[fbUID] = userID
	}
	
	if (twitterUID) {
		twitterUsers[twitterUID] = userID
	}
	
	if (googleUID) {
		googleUsers[googleUID] = userID
	}
}

router.post('/', function (req, res) {
	// console.log(');
	
	var user = req.body
	
	log.d("users",user);

	log.d("users","firstname:",user.firstName);
	log.d("users","lastname:",user.lastName);

	log.d("users","fbUID", user.fbUID);
	log.d("users","twitterUID", user.twitterUID);
	log.d("users","googleUID", user.googleUID);

	// console.log(req.body.hasOwnProperty('username'));

	// res.send(JSON.stringify({status:"created"}));
	var fbUID = user.fbUID
	var twitterUID = user.twitterUID
	var googleUID = user.googleUID
	
	var userID = facebookUsers[fbUID] || twitterUsers[twitterUID] || googleUsers[googleUID]	//TODO: make sure these are all the same
	
	if (userID === undefined) {
		log.i("users","Creating new user")
		user.userID = users.length
		users.push(user)
		userID = users.length-1
		
		log.d("users","update backlinks")
		updateSocialBacklinks(user)
		
		res.send({"status":"created", "userID":userID});
	} else {
		var user = users[userID]
		res.send(409,user)
	}
});

router.get('/:id', function(req,res) {
	var userID = parseInt(req.params.id)
	log.d("users","getting user by id",userID,users.length)
	var user = users[userID]
	if (userID && user) {
		res.send(user)
	} else {
		res.send(404,{"status":"user not found"})
	}
})

router.put('/:id', function(req,res) {
	var userID = parseInt(req.params.id)
	var user = users[userID]
	if (userID && user) {
		for (var i in req.body) {
			user[i] = req.body[i]
			updateSocialBacklinks(user)
		}
	} else {
		res.send(404,{"status":"user not found"})
	}
})

module.exports = router;
