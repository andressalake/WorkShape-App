//receive team name, and one email
//post request

var express = require('express'); // use express frmework

var router = express.Router(); //

var teams = [] //vector of teams
router.post('/', function (req, res){
            
            log.d("****", req.body)
            var teamName = req.body.teamName;
            var email = req.body.emailTeam;
            var adminID = req.body.adminID;
            
            if(teamName != null && email != null && adminID != null)
            {
                var teamID = teams.length
                teams.push(teamID);
                res.send({"status" : "Created", "teamID" : teamID});
                log.d("Created", "Team ID" + teamID)
            
            } else {
                res.send(400, {"status" : "failed", "body": req.body});
            
            }
            
            });

//--------eu mexi aqui trying to get team iDs
//this need to be fixed

router.get('/:id',function(req, res){
           log.d("****** Parameters", req.params.id)
           var teamID = parseInt(req.params.id)
           log.d("trying to get teamID__________")
           
           var team = teams[teamID]
           if(teamID && team){
                res.send(team)
                log.d("******* Team variable", team)
           }
           else{
           res.send(404,{"status":"team not found"})
                }
           
           });



//-------finish mexi

module.exports = router;

//send a json object


//give an response

