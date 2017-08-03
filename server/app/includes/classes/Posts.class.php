<?php

class Posts extends Models {

    protected static $table = "post";

    public static function createDir($path) {

        return ((!is_dir($path)) ? @mkdir($path, 0777) : TRUE);
    }

    public static function relation($rs) {

        return array("PostLike" => "post_id",
            "PostComment" => "post_id",
            "PostReport" => "post_id",
        );
    }

    public static function deletePost($rs) {
        $rs = array_map("security", $rs);

        if (!Users::isUser($rs['userid']) || !Posts::isPost($rs['post_id']))
            return array("status" => "false", "msg" => "invalid credentials");

        if (Posts::postUser($rs['post_id']) != $rs['userid'])
            return array("status" => "false", "msg" => "you don't have permission to delete another member post");

        foreach (Posts::relation() as $relationTbl => $relationClm)
            $relationTbl::remove("$relationClm=" . $rs['post_id']);

        ActivityLog::remove("main_id = '" . $rs['post_id'] . "' and (type='COMMENTPOST' or type='LIKEPOST' or type='DISLIKEPOST' or type='RATEPOST')");
        HashTag::remove("relative_id = '" . $rs['post_id'] . "' and type='post'");
        Posts::remove("post_id='" . $rs['post_id'] . "'");

        return array("status" => "true", "msg" => "Post deleted succesfully");
    }

    public static function postByHash($rs) {
        $rs = array_map("security", $rs);
        $hashtag = $rs['hashtag'];
        $hashPostsData = [];
        $userid = $rs['userid'];

        if (!Users::isUser($rs['userid']))
            return array("status" => "false", "msg" => "invalid user");
        $condIsPrivate = Users::isPrivateCond($userid, "p.user_id");
        $condBlock = UserBlock::blockUserCond($userid, "p.user_id");
        $condDeact = Users::deactivateUserCond($userid, "p.user_id");


//        $hashPosts = Posts::query(""
//                    . "select * FROM `hashtags` as hash LEFT JOIN post_comment AS pc on pc.id = hash.relative_id left join post as p on (pc.post_id = p.post_id OR hash.relative_id = p.post_id)  "
//                    . " where  hash.tag = '$hashtag'  $condBlock $condDeact $condIsPrivate group by p.post_id ");
        $hashPosts = Posts::query("select hash.tag,hash.type,hash.id,p.post_id,p.user_id,p.data_type,p.data_url,p.thumb_image"
                        . " FROM `hashtags` as hash inner join post as p on if(hash.type='COMMENT',(select pc.post_id from post_comment as  pc where pc.id=hash.relative_id),hash.relative_id)=p.post_id  and p.suspend='0'"
                        . "   where  hash.tag='$hashtag' $condBlock $condDeact $condIsPrivate group by p.post_id,hash.tag");



//  $hashPosts=Posts::query("select p.post_id,p.user_id,p.data_type,p.data_url,p.thumb_image"
//                . " FROM `hashtags` as hash left join post as p on hash.relative_id=p.post_id  and p.suspend='0' where  hash.tag='$hashtag' and hash.type='post' $condBlock $condDeact $condIsPrivate");



        if ($hashPosts->size() > 0) {
            while ($row = $hashPosts->fetch()) {

                $row['post_id'] = $row['post_id'];

                $hashPostsData[] = $row;
            }
        }

        return Posts::isNullArray(array("total_data" => count($hashPostsData),
                    "hash_posts" => $hashPostsData,
                    "status" => "true",
                    "msg" => "success"
        ));
    }

    public static function popularPosts($rs) {
        $rs = array_map("security", $rs);
        $popularPostsData = [];
        $page = (int) $rs['page'] == 0 ? 1 : (int) $rs['page'];
        $pageLower = ($page - 1) * 30;
        $pageUpper = 15;

        if (!Users::isUser($rs['userid']))
            return array("status" => "false", "msg" => "invalid credentials");

        $condIsPrivate = Users::isPrivateCond($rs['userid']);
        $condBlock = UserBlock::blockUserCond($rs['userid']);
        $condDeact = Users::deactivateUserCond($rs['userid']);
        $postcount = Posts::count("suspend='0'  $condBlock $condDeact $condIsPrivate")->count;
        $total_page = ceil($postcount / 30);

        $popularPosts = Posts::query("select p.post_id,p.user_id,p.data_type,p.data_url,p.thumb_image,"
                        . "(select sum(like_status) from post_like as pl where pl.post_id=p.post_id ) as like_count,"
                        . "(select count(id) from post_comment as pc where pc.post_id=p.post_id ) as commment_count"
                        . " FROM `post` as p where suspend='0'  $condBlock  $condDeact $condIsPrivate  order by dtdate  DESC  limit $pageLower,$pageUpper  ");
//                        . " FROM `post` as p where suspend='0'  $condBlock  $condDeact $condIsPrivate  order by dtdate if(like_count IS NULL,0,like_count) + if(commment_count IS NULL,0,commment_count) DESC  limit $pageLower,$pageUpper  ");

        if ($popularPosts->size() > 0) {
            while ($row = $popularPosts->fetch()) {
                $row['title'] = ($row['title']);
                $popularPostsData[] = $row;
            }
        }

        return Posts::isNullArray(array("total_data" => count($popularPostsData),
                    "Popular_posts" => $popularPostsData,
                    "total_page" => $total_page,
                    "total_records" => $postcount,
                    "status" => "true",
                    "msg" => "success"
        ));
    }

    public function userPostdata($id, $pageNo) {
        global $db;
        $friendid = $id;
        $UserImageVideo = [];
        $UserImagePost = [];
        if (!Users::isUser($friendid))
            return array("status" => "false", "msg" => "invalid credentials");

        if ($pageNo == '' || $pageNo == 0) {
            $pageNo = 1;
        }
        $pageSize = 10;
        $total_rows = 0;
//echo $pageNo;

        if ($pageNo <= 1) {
            $UserPosts = Posts::getAll("user_id='$friendid' and  suspend='0' order by dtdate DESC limit 0,$pageSize  ");
foreach ($UserPosts as $post) {

$followStatus=Follow::followStatus($post->user_id,$friendid);
$post->follow_status = ($followStatus=="no")? 0 : $followStatus;

            $post->post_total_like = PostLike::count("post_id=$post->post_id AND like_status = '1'  $condDeact $condBlock")->count;
            $post->post_total_dislike = PostLike::count("post_id=$post->post_id AND like_status = '-1'  $condDeact $condBlock")->count;
            $post->post_total_comment = PostComment::count("post_id='$post->post_id' $condDeact $condBlock")->count;
            $post->is_post_liked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'")->count;
            $post->is_post_disliked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '-1'")->count;
$post->is_post_favourate = PostLike::count("post_id=$post->post_id and user_id='$userid' AND fav_status=1")->count;
            $post->is_post_rated = PostRate::count("post_id=$post->post_id and user_id='$userid' ")->count;
            $post->average_rate = PostRate::average_rate($post->post_id);
            $userDetail = Users::get("userid='$post->user_id'", "user_thumbimage,user_name,concat(fname,' ',lname) as user_name");
            $post->user_image = $userDetail->user_thumbimage;
            $post->user_name = $userDetail->user_name;
            $post->full_name = $userDetail->full_name;
            $post->last_three_comments = PostComment::allUserComments($userid, $post->post_id, "order by pc.id DESC", 'limit 3');
//            if(count($post->last_three_comments) > 0){
//                $post->count_comment = 1;
//            }else{
//                $post->count_comment = 0;
//            }

            $is_comment = PostComment::isMyComment($userid, $post->post_id);
//            print_r($is_comment);
//            die;

            $post->count_comment = $is_comment;
$post->time_ago = USER_CLASS::getTimeInfo($post->dtdate, date('Y-m-d H:i:s'), 'x');
				}
        } else {
            $no = ($pageNo - 1) * $pageSize;
            $pageSize1=$pageSize*$pageNo;
            $UserPosts = Posts::getAll("user_id='$friendid' and  suspend='0'  order by dtdate DESC limit $no,$pageSize1 ");
foreach ($UserPosts as $post) {

            $post->post_total_like = PostLike::count("post_id=$post->post_id AND like_status = '1'  $condDeact $condBlock")->count;
            $post->post_total_dislike = PostLike::count("post_id=$post->post_id AND like_status = '-1'  $condDeact $condBlock")->count;
            $post->post_total_comment = PostComment::count("post_id='$post->post_id' $condDeact $condBlock")->count;
            $post->is_post_liked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'")->count;
            $post->is_post_disliked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '-1'")->count;
$post->is_post_favourate = PostLike::count("post_id=$post->post_id and user_id='$userid' AND fav_status=1")->count;
            $post->is_post_rated = PostRate::count("post_id=$post->post_id and user_id='$userid' ")->count;
            $post->average_rate = PostRate::average_rate($post->post_id);
           
            $post->last_three_comments = PostComment::allUserComments($userid, $post->post_id, "order by pc.id DESC", 'limit 3');
//            if(count($post->last_three_comments) > 0){
//                $post->count_comment = 1;
//            }else{
//                $post->count_comment = 0;
//            }

            $is_comment = PostComment::isMyComment($userid, $post->post_id);
//            print_r($is_comment);
//            die;

            $post->count_comment = $is_comment;
$post->time_ago = USER_CLASS::getTimeInfo($post->dtdate, date('Y-m-d H:i:s'), 'x');
				}
        }
//     $UserPosts =  Posts::getAll("user_id='$friendid' and  suspend='0'  order by dtdate ","post_id,user_id,data_type,data_url,thumb_image");

        $sql = "SELECT * FROM post WHERE user_id = '" . $friendid . "' and suspend='0' ";
        $result1 = $db->query($sql);

        $total_rows = $result1->size();
//echo  $total_rows;
        $lastPage = ceil($total_rows / $pageSize);

        foreach ($UserPosts as $post) {
            if ($post->data_type == "audio")
                $UserImagePost[] = $post;
            else
                $UserImageVideo[] = $post;
        }
        return Posts::isNullArray(array("total_image_data" => count($UserImagePost),
                    "total_video_data" => count($UserImageVideo),
                    "post" => $UserPosts,
                    "totalrecord" => $total_rows,
                    "totalpage" => $lastPage
        ));

//        return Posts::isNullArray(array("total_image_data" => count($UserImagePost),
//                    "total_video_data" => count($UserImageVideo),
//                    "image_data" => $UserImagePost,
//                    "video_data" => $UserImageVideo
//        ));
    }

    public static function getPost($rs) {
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $postid = (int) $rs['post_id'];

        $condIsPrivate = Users::isPrivateCond($userid);
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);

        if (!Users::isUser($userid) || !Posts::isPost($postid))
            return array("status" => "false", "msg" => "invalid credentials");

        $post = Posts::get("post_id=$postid  $condBlock $condDeact");
        $post->title = ($post->title);
         $post->post_total_like = PostLike::count("post_id=$post->post_id AND like_status = '1'  $condDeact $condBlock")->count;
            $post->post_total_dislike = PostLike::count("post_id=$post->post_id AND like_status = '-1'  $condDeact $condBlock")->count;
        $post->post_total_comment = PostComment::count("post_id='$post->post_id' $condDeact $condBlock")->count;

        $post->is_post_liked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'")->count;
        $post->is_post_disliked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '-1'")->count;
        $post->is_post_rated = PostRate::count("post_id=$post->post_id and user_id='$userid' ")->count;
        $post->average_rate = PostRate::average_rate($post->post_id);
        $userDetail = Users::get("userid='$post->user_id'", "user_thumbimage,user_name,concat(fname,' ',lname) as full_name");
        $post->user_image = $userDetail->user_thumbimage;
        $post->user_name = $userDetail->user_name;
        $post->full_name = $userDetail->full_name;
        $post->time_ago = Utility::getTimeAgo($post->dtdate);
        $post->block_relation = UserBlock::count("(user_id='$post->user_id' and block_user_id='$userid') or "
                        . "(block_user_id='$post->user_id' and user_id='$userid') ")->count;
        $post->last_three_comments = PostComment::allUserComments($userid, $post->post_id, "order by pc.id DESC", 'limit 3');
        if (count($post->last_three_comments) > 0) {
            $post->count_comment = 1;
        } else {
            $post->count_comment = 0;
        }
        $arr = array('status' => "true",
            "posts" => $post,
        );
        return Posts::isNullArray($arr);
    }


    
    public static function homePosts($rs) {
        header('Content-Type: application/json');
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $page = (int) $rs['page'];
        $pageLower = ($page - 1) * 15;
        $pageUpper = 15;

        if (!Users::isUser($rs['userid']))
            return array("status" => "false", "msg" => "invalid user id");
        $Connections = Follow::userConnectionList($userid);
        $condIsPrivate = Users::isPrivateCond($userid);
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        $postcount = Posts::count("suspend='0' and user_id in($Connections) $condBlock $condDeact $condIsPrivate")->count;
        $total_page = ceil($postcount / 15);
        $posts = Posts::getAll("suspend='0' and user_id  in($Connections)  $condBlock  $condDeact $condIsPrivate order by dtdate DESC limit $pageLower,$pageUpper ");

        foreach ($posts as $post) {

            $post->post_total_like = PostLike::count("post_id=$post->post_id AND like_status = '1'  $condDeact $condBlock")->count;
            $post->post_total_dislike = PostLike::count("post_id=$post->post_id AND like_status = '-1'  $condDeact $condBlock")->count;
            $post->post_total_comment = PostComment::count("post_id='$post->post_id' $condDeact $condBlock")->count;
            $post->is_post_liked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'")->count;
            $post->is_post_disliked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '-1'")->count;
            $post->is_post_rated = PostRate::count("post_id=$post->post_id and user_id='$userid' ")->count;
            $post->average_rate = PostRate::average_rate($post->post_id);
            $userDetail = Users::get("userid='$post->user_id'", "user_thumbimage,user_name,concat(fname,' ',lname) as user_name");
            $post->user_image = $userDetail->user_thumbimage;
            $post->user_name = $userDetail->user_name;
            $post->full_name = $userDetail->full_name;
            $post->last_three_comments = PostComment::allUserComments($userid, $post->post_id, "order by pc.id DESC", 'limit 3');
//            if(count($post->last_three_comments) > 0){
//                $post->count_comment = 1;
//            }else{
//                $post->count_comment = 0;
//            }

            $is_comment = PostComment::isMyComment($userid, $post->post_id);
//            print_r($is_comment);
//            die;

            $post->count_comment = $is_comment;


            $post->time_ago = USER_CLASS::getTimeInfo($post->dtdate, date('Y-m-d H:i:s'), 'x');
        }
        $arr = array('status' => "true",
            "posts" => $posts,
            "total_page" => $total_page,
            "total_records" => $postcount);
        return Posts::isNullArray($arr);
    }
	
	
	public static function homePostsIJBT($rs) {
        header('Content-Type: application/json');
              $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $page = (int) $rs['page'];
        $pageLower = ($page - 1) * 5;
        $pageUpper = 5;

        if (!Users::isUser($rs['userid']))
            return array("status" => "false", "msg" => "invalid user id");
        $Connections = Follow::userConnectionList($userid);
        $condIsPrivate = Users::isPrivateCond($userid);
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        //$postcount = Posts::count("suspend='0' and user_id in($Connections) $condBlock $condDeact $condIsPrivate or usertype='0' or usertype='1'")->count;
$postcount = Posts::count("suspend='0' and user_id not in($userid) and (user_id in($Connections) or (usertype=0 or usertype=1)) $condBlock $condDeact $condIsPrivate")->count;
        $total_page = ceil($postcount / 5);
        //$posts = Posts::getAll("suspend='0' and user_id  in($Connections)  $condBlock  $condDeact $condIsPrivate or usertype='0' or usertype='1' order by dtdate DESC limit $pageLower,$pageUpper ");
//$posts = Posts::getAll("suspend='0' and user_id not in($userid) and (user_id in($Connections) or (usertype=0 or usertype=1)) $condBlock $condDeact $condIsPrivate order by dtdate DESC limit $pageLower,$pageUpper ");
$posts = Posts::getAll("suspend='0' and user_id not in($userid) and (user_id in($Connections) or (usertype=0 or usertype=1 )) $condBlock $condDeact $condIsPrivate order by  FIELD( usertype, '0', '2', '1' ) ");
//echo "suspend='0' and user_id not in($userid) and (user_id in($Connections) or (usertype=0 or usertype=1 )) $condBlock $condDeact $condIsPrivate order by  FIELD( usertype, '0', '2', '1' ) limit $pageLower,$pageUpper";
$postlikeres=array();
        foreach ($posts as $post) {

$followStatus=Follow::followStatus($post->user_id,$userid);
$post->follow_status = ($followStatus=="no")? 0 : $followStatus;


            $post->post_total_like = PostLike::count("post_id=$post->post_id AND like_status = '1'  $condDeact $condBlock")->count;
            $post->post_total_dislike = PostLike::count("post_id=$post->post_id AND like_status = '-1'  $condDeact $condBlock")->count;
            $post->post_total_comment = PostComment::count("post_id='$post->post_id' $condDeact $condBlock")->count;
            $post->is_post_liked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'")->count;

            $post->is_post_disliked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '-1'")->count;
            $post->is_post_rated = PostRate::count("post_id=$post->post_id and user_id='$userid' ")->count;
$host = 'localhost';
$user = 'ijbt_app';
$pass = 'Pluggdd@2016';
$dbname = 'ijbt_post';
$connectionstring=mysqli_connect($host,$user,$pass,$dbname);

			
			//$sql_fav=mysqli_query($connectionstring,"SELECT * FROM  `post_like`  where post_id=$post->post_id and user_id='$userid' and fav_status=1");
$sql_fav=mysqli_query($connectionstring,"SELECT * FROM  `post_like`  where post_id=$post->post_id and user_id='$userid' order by id desc Limit 1");
			if(!$sql_fav)
				echo mysqli_error($connectionstring);
//$post->is_post_favourate = mysqli_num_rows($sql_fav);
$favourate=mysqli_fetch_assoc($sql_fav);
			$post->is_post_favourate = ($favourate['fav_status']=='1')?1:0;
            $post->average_rate = PostRate::average_rate($post->post_id);
            $userDetail = Users::get("userid='$post->user_id'", "user_thumbimage,user_name,concat(fname,' ',lname) as user_name");
            $post->user_image = $userDetail->user_thumbimage;
            $post->user_name = $userDetail->user_name;
            $post->full_name = $userDetail->full_name;
            $post->last_three_comments = PostComment::allUserComments($userid, $post->post_id, "order by pc.id DESC", 'limit 3');
//            if(count($post->last_three_comments) > 0){
//                $post->count_comment = 1;
//            }else{
//                $post->count_comment = 0;
//            }

            $is_comment = PostComment::isMyComment($userid, $post->post_id);
//            print_r($is_comment);
//            die;

            $post->count_comment = $is_comment;

			
			
            $post->time_ago = USER_CLASS::getTimeInfo($post->dtdate, date('Y-m-d H:i:s'), 'x');

if(($post->is_post_liked !=1 )&&($post->is_post_disliked !=1)&&($post->is_post_favourate !=1))
$postlikeres[]=$post;
        }
// print_r($postlikeres);
$postcount = count($postlikeres);
        $total_page = ceil($postcount / 5);
		$postlikeres=array_slice($postlikeres,$pageLower,$pageUpper,false);
       // print_r($postlikeres);
        $arr = array('status' => "true",
            "posts" => $postlikeres,
            "total_page" => $total_page,
            "total_records" => $postcount);
        return Posts::isNullArray($arr);
    }

public static function homePostsfav($rs) {
        header('Content-Type: application/json');
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $page = (int) $rs['page'];
        $pageLower = ($page - 1) * 5;
        $pageUpper = 5;

        if (!Users::isUser($rs['userid']))
            return array("status" => "false", "msg" => "invalid user id");
        $Connections = Follow::userConnectionList($userid);
        $condIsPrivate = Users::isPrivateCond($userid);
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        //$postcount = Posts::count("suspend='0' and user_id in($Connections) $condBlock $condDeact $condIsPrivate")->count;
$postcount = Posts::count("suspend='0' and (user_id in($Connections) or (usertype=0 or usertype=1)) $condBlock $condDeact $condIsPrivate")->count;
        $total_page = ceil($postcount / 5);
$postlikeres=array();
        //$posts = Posts::getAll("suspend='0' and user_id  in($Connections)  $condBlock  $condDeact $condIsPrivate order by dtdate DESC limit $pageLower,$pageUpper ");
//$posts = Posts::getAll("suspend='0' and user_id  in($Connections)  $condBlock  $condDeact $condIsPrivate order by dtdate DESC");
$posts = Posts::getAll("suspend='0' and (user_id in($Connections) or (usertype=0 or usertype=1))  $condBlock  $condDeact $condIsPrivate order by dtdate DESC");
        foreach ($posts as $post) {

         $followStatus=Follow::followStatus($post->user_id,$userid);
         $post->follow_status = ($followStatus=="no")? 0 : $followStatus;

            $post->post_total_like = PostLike::count("post_id=$post->post_id AND like_status = '1'  $condDeact $condBlock")->count;
            $post->post_total_dislike = PostLike::count("post_id=$post->post_id AND like_status = '-1'  $condDeact $condBlock")->count;
            $post->post_total_comment = PostComment::count("post_id='$post->post_id' $condDeact $condBlock")->count;
            $post->is_post_liked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'")->count;

            $post->is_post_disliked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '-1'")->count;
            $post->is_post_rated = PostRate::count("post_id=$post->post_id and user_id='$userid' ")->count;
$post->is_post_favourate = PostLike::count("post_id=$post->post_id and user_id='$userid' AND fav_status=1")->count;
$fav= PostLike::count("post_id=$post->post_id and user_id='$userid' AND fav_status=1")->count;
            $post->average_rate = PostRate::average_rate($post->post_id);
            $userDetail = Users::get("userid='$post->user_id'", "user_thumbimage,user_name,concat(fname,' ',lname) as user_name");
            $post->user_image = $userDetail->user_thumbimage;
            $post->user_name = $userDetail->user_name;
            $post->full_name = $userDetail->full_name;
            $post->last_three_comments = PostComment::allUserComments($userid, $post->post_id, "order by pc.id DESC", 'limit 3');
//            if(count($post->last_three_comments) > 0){
//                $post->count_comment = 1;
//            }else{
//                $post->count_comment = 0;
//            }

            $is_comment = PostComment::isMyComment($userid, $post->post_id);
//            print_r($is_comment);
//            die;

            $post->count_comment = $is_comment;

			
			
            $post->time_ago = USER_CLASS::getTimeInfo($post->dtdate, date('Y-m-d H:i:s'), 'x');
if($fav)
$postlikeres[]=$post;
        }
$postcount = count($postlikeres);
        $total_page = ceil($postcount / 5);
$postlikeres=array_slice($postlikeres,$pageLower,$pageUpper);
//$postlikeres= array_filter($array, function($posts) { return $v['is_post_liked'] == 1; });
        $arr = array('status' => "true",
            "posts" => $postlikeres,
            "total_page" => $total_page,
            "total_records" => $postcount);
        return Posts::isNullArray($arr);
    }
	

public static function homePostslike($rs) {
        header('Content-Type: application/json');
        $rs = array_map("security", $rs);
        $userid = (int) $rs['userid'];
        $page = (int) $rs['page'];
        $pageLower = ($page - 1) * 5;
        $pageUpper = 5;

        if (!Users::isUser($rs['userid']))
            return array("status" => "false", "msg" => "invalid user id");
        $Connections = Follow::userConnectionList($userid);
        $condIsPrivate = Users::isPrivateCond($userid);
        $condBlock = UserBlock::blockUserCond($userid);
        $condDeact = Users::deactivateUserCond($userid);
        //$postcount = Posts::count("suspend='0' and user_id in($Connections) $condBlock $condDeact $condIsPrivate")->count;
$postcount = Posts::count("suspend='0' and (user_id in($Connections) or (usertype=0 or usertype=1)) $condBlock $condDeact $condIsPrivate")->count;
        $total_page = ceil($postcount / 5);
$postlikeres=array();
        //$posts = Posts::getAll("suspend='0' and user_id  in($Connections)  $condBlock  $condDeact $condIsPrivate order by dtdate DESC limit $pageLower,$pageUpper ");
//$posts = Posts::getAll("suspend='0' and user_id  in($Connections)  $condBlock  $condDeact $condIsPrivate order by dtdate DESC");
$posts = Posts::getAll("suspend='0' and (user_id in($Connections) or (usertype=0 or usertype=1))  $condBlock  $condDeact $condIsPrivate order by dtdate DESC");
//die("suspend='0' and user_id  in($Connections)  $condBlock  $condDeact $condIsPrivate order by dtdate DESC limit $pageLower,$pageUpper ");

        foreach ($posts as $post) {

            $followStatus=Follow::followStatus($post->user_id,$userid);
            $post->follow_status = ($followStatus=="no")? 0 : $followStatus;


            $post->post_total_like = PostLike::count("post_id=$post->post_id AND like_status = '1'  $condDeact $condBlock")->count;
            $post->post_total_dislike = PostLike::count("post_id=$post->post_id AND like_status = '-1'  $condDeact $condBlock")->count;
            $post->post_total_comment = PostComment::count("post_id='$post->post_id' $condDeact $condBlock")->count;
            $post->is_post_liked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'")->count;

            $post->is_post_disliked = PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '-1'")->count;
            $post->is_post_rated = PostRate::count("post_id=$post->post_id and user_id='$userid' ")->count;
$post->is_post_favourate = PostLike::count("post_id=$post->post_id and user_id='$userid' AND fav_status=1")->count;
$fav= PostLike::count("post_id=$post->post_id and user_id='$userid' AND like_status = '1'  $condDeact $condBlock")->count;
            $post->average_rate = PostRate::average_rate($post->post_id);
            $userDetail = Users::get("userid='$post->user_id'", "user_thumbimage,user_name,concat(fname,' ',lname) as user_name");
            $post->user_image = $userDetail->user_thumbimage;
            $post->user_name = $userDetail->user_name;
            $post->full_name = $userDetail->full_name;
            $post->last_three_comments = PostComment::allUserComments($userid, $post->post_id, "order by pc.id DESC", 'limit 3');
//            if(count($post->last_three_comments) > 0){
//                $post->count_comment = 1;
//            }else{
//                $post->count_comment = 0;
//            }

            $is_comment = PostComment::isMyComment($userid, $post->post_id);
//            print_r($is_comment);
//            die;

            $post->count_comment = $is_comment;

			
			
            $post->time_ago = USER_CLASS::getTimeInfo($post->dtdate, date('Y-m-d H:i:s'), 'x');
if($fav)
$postlikeres[]=$post;
        }
$postcount = count($postlikeres);
        $total_page = ceil($postcount / 5);
$postlikeres=array_slice($postlikeres,$pageLower,$pageUpper);
//$postlikeres= array_filter($array, function($posts) { return $v['is_post_liked'] == 1; });
        $arr = array('status' => "true",
            "posts" => $postlikeres,
            "total_page" => $total_page,
            "total_records" => $postcount);
        return Posts::isNullArray($arr);
    }
	
    public static function uploadPostData($files, $userid, $post_id) {

        if ($files['name'] != "") {
            $isCreatedPost = Posts::createDir("../uploads/posts");
            $isCreatedPostid = Posts::createDir("../uploads/posts/$userid");
            $isCreatedPostFld = Posts::createDir("../uploads/posts/$userid/$post_id");

            if ($isCreatedPost && $isCreatedPostid && $isCreatedPostFld) {

                $file_name = randomcode(5) . time() . str_replace(array(" ", "'", "\'", "_"), "", $files['name']);
                move_uploaded_file($files['tmp_name'], "../uploads/posts/$userid/$post_id/$file_name");
                Posts::save(array("data_url" => "uploads/posts/$userid/$post_id/$file_name"), "post_id=$post_id");
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static function uploadPostImage($files, $userid, $post_id) {
        $isCreatedPost = ("../uploads/posts");
        $isCreatedPostid = ("../uploads/posts/$userid");
        $isCreatedPostFld = ("../uploads/posts/$userid/$post_id");
        $isCreatedPostFld = Posts::createDir("../uploads/posts");
        $isCreatedPostFld = Posts::createDir("../uploads/posts/$userid");
        $isCreatedPostFld = Posts::createDir("../uploads/posts/$userid/$post_id");

        if (!file_exists($isCreatedPost)) {
            @mkdir($isCreatedPost, 0777, true);
        }
        if (!file_exists($isCreatedPostid)) {
            @mkdir($isCreatedPostid, 0777, true);
        }
        if (!file_exists($isCreatedPostFld)) {
            mkdir($isCreatedPostFld, 0777, true);
        }

//        if ($isCreatedPost && $isCreatedPostid && $isCreatedPostFld) {

        $file_name = randomcode(5) . time() . str_replace(array(" ", "'", "\'", "_"), "", $files['name']);
        $IMAGE = new UPLOAD_IMAGE();
        $IMAGE->init($files['tmp_name']);
        move_uploaded_file($files['tmp_name'], "../uploads/posts/$userid/$post_id/$file_name");
        $file_namethumb = explode(".", $file_name);
        $file_nameExt = end($file_namethumb);
        $file_namethumb = $file_namethumb[0] . "-thumb" . ".$file_nameExt";
        $IMAGE->resizeImage(200, 200, 'crop');
        $IMAGE->saveImage("../uploads/posts/$userid/$post_id/$file_namethumb", 75);

        Posts::save(array("image" => "uploads/posts/$userid/$post_id/$file_name",
            "thumb_image" => "uploads/posts/$userid/$post_id/$file_namethumb"), "post_id=$post_id");
        return true;
//        } else {
//            return false;
//        }
    }

    public static function addPost($rs) {
        $title = ($rs['title']);
        $rs = array_map("security", $rs);
        $userid = $rs['userid'];
        $type = $rs['data_type'];
        $usertype=$rs['user_type'];
        $device_type	=	$rs['device_type'];


        $hashtags = explode(",", $rs['hashtags']);
        $tagged_users = explode(",", $rs['tagged_users']);
        $data = $_FILES['data'];
        $image = $_FILES['image'];
        $msg = "Post addedd succesfully";
        if (!Users::isUser($rs['userid']))
		//return $title;
            return array("status" => "false", "msg" => "invalid user id ");

        if ($type == "audio") {
            if (isset($data['name']) && $data['name'] != "") {
                $extenstionArr = explode(".", $data['name']);
                $extenstion = end($extenstionArr);
                if ($extenstion == "m4a" || $extenstion == "mp3") {
                    
                } else {
                    return array("status" => "false",
                        "msg" => "Please provide valid file format(only m4a for audio)");
                }
            }
        } 
//        else if ($type == "video") {
//            $extenstionArr = explode(".", $data['name']);
//            $extenstion = end($extenstionArr);
//            if ($extenstion != "mp4") {
//                return array("status" => "false",
//                    "msg" => "Please provide valid file format(only mp4 for video)");
//            }
//        }
        $cat=$_POST["category"];
        $placebought=$_POST["placebought"];
        $currency=$_POST["currency"];
        $price=$_POST["price"];
        //$comment=$_POST["comment"];

        $post_id = Posts::save(array("title" => $title,
                    "data_type" => $type,
                    "user_id" => $userid,
                     "category"=>$cat,
                     "placebought"=>$placebought,
                     "currency"=>$currency,
                     "price"=>$price,
                     "usertype"=> $usertype,
                     //"comment"=>$comment,
                    "dtdate" => date("Y-m-d H:i:s", time())));
        TagUsers::addUserTags($tagged_users, $post_id);
        HashTag::addHashTags($hashtags, $post_id);
        PushNotification::mentionUser($rs['userid'], $tagged_users, "post");
        if ($type == "video" and $device_type == "android") {
            $videoPath = $rs['video_path'];
            $VIDEO = new UPLOAD_VIDEO;
            $pathArr = $VIDEO->saveThumbnail($post_id, $userid, $videoPath);
            $thumbPath = $pathArr['thumb'];
            $videoPath = $pathArr['video'];
            $VIDEO->saveVideo($post_id, $thumbPath, $videoPath);
        } else {
            if (!Posts::uploadPostData($data, $userid, $post_id)) {
                $msg = "Post added succesfully but media not uploaded";
            }
//            if (!empty($data['name'])) {
//                if (!self::uploadPostImage($image, $userid, $post_id)) {
//                    $msg = "Post added succesfully but media image not uploaded";
//                }
//            }
        }

        if (!Posts::uploadPostImage($image, $userid, $post_id)) {
            $msg = "Post added succesfully but media image not uploaded";
        }

        $postdetail = Posts::get("post_id=$post_id", "data_type,data_url,image,thumb_image");

        return Posts::isNullArray(array("status" => "true",
                    "msg" => $msg,
                    "post_id" => $post_id,
                    "type" => $postdetail->data_type,
                    "image" => $postdetail->image,
                    "data_url" => $postdetail->data_url));
    }
    
    
    
     public static function uploadVideoThumb($files,$userid,$post_id){
         
                $isCreatedPost=createDir("../uploads/posts");
		$isCreatedPostid=createDir("../uploads/posts/$userid");
                $isCreatedPostFld=createDir("../uploads/posts/$userid/$post_id");

                if($isCreatedPost && $isCreatedPostid && $isCreatedPostFld ){
                    $file_name	=	randomcode(5).time().str_replace(array(" ","'","\'","_"),"",$files['name']); 
                    $IMAGE = new UPLOAD_IMAGE();
	            $IMAGE->init($files['tmp_name']);
		    move_uploaded_file($files['tmp_name'], "../uploads/posts/$userid/$post_id/$file_name");
                    self::save(array("image"=>"uploads/posts/$userid/$post_id/$file_name",
                        "thumb_image"=>"uploads/posts/$userid/$post_id/$file_name"),"post_id=$post_id");
                    return true;
                }
                else{
                    return false;
                }
		
     }
    public static function isPost($postid) {
        return (int) $postid > 0 && Posts::count("post_id=$postid")->count > 0 ? true : false;
    }

    public static function postUser($postid) {
        if ((int) $postid > 0)
            return Posts::get("post_id=$postid", "user_id")->user_id;
        else
            return 0;
    }

}
