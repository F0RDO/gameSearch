totalWinAndLose = (matchInfoList, result) => {
    let recordDiv = document.getElementById('recent-victory');
    let wins = 0;
    let loses = 0;
    let totalGame = 0;
    let arrMatchInfo = JSON.parse(matchInfoList);
    let jsonResult = JSON.parse(result);
    Array.from(arrMatchInfo).forEach((matchInfo)=>{
        Array.from(matchInfo.info.participants).forEach((participants)=>{
           if(participants.summonerName == jsonResult.name){
                participants.win == true ? wins++ : loses++;
                totalGame++;
           }
        });
    });
    recordDiv.textContent = totalGame + '전 ' + wins + '승 ' + loses + '패';
};


detailArrowUpDown = (matchId, upOrDown) => {
    let detailUpOne = document.getElementById('detailUp_'+matchId);
    let detailDownOne = document.getElementById('detailDown_'+matchId);
    if(upOrDown == 'up'){
        detailUpOne.style.display = 'none';
        detailDownOne.style.display = 'block';
        $('#recent_match_'+matchId).hide();
    }else {
        detailDownOne.style.display = 'none';
        detailUpOne.style.display = 'block';
        $('#recent_match_'+matchId).show();
    }
}

makeDetailInfo = (matchId, userName, version, matchInfo) => {
    //$('#recent_match_'+matchId).load('recentDetailInfo.do?matchId='+matchId+'&userName='+userName+'&version='+version);

}