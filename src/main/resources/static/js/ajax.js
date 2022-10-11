   
    /*<![CDATA[*/

 
      $(document).ready(function() {

        //모달 띄우기 
        $('#contents').on('click', 'a', function (e) {

          e.preventDefault();

          $('#modifyForm').hide();
          $('#addForm').show();
          post_id = $(this).data('id');

          //게시글 상세 조회
          $.ajax({
            url: "/polaroid/posts/" + post_id,
            type: 'GET',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            success: function (data) {
              getPostTitle(data);
              getPostPhoto(data);
              getPostAccount(data);
              getBtn(data);

              if (data.ispostLike == 1) {
                $('#heart').removeClass("bx bx-heart");   //빈 하트 사라짐
                $('#heart').addClass("bx bxs-heart");     //하트 생김           
                $('#postLikeCount').html(data.postLikeCount);
              } else {
                $('#heart').removeClass("bx bxs-heart");   // 하트 사라짐
                $('#heart').addClass("bx bx-heart");      //빈 하트 
                $('#postLikeCount').html(data.postLikeCount);
              }
            },
            fail: function (ex) {
              console.log("error: ", ex);
            }
          });
          
          //게시글 조회수 증가
          $.ajax({
            url: "/polaroid/postView/" + post_id,
            type: 'PUT',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
              postView: $('#postView').val()
            }),
            success: function (data) {
              console.log("성공");
            },
            fail: function (ex) {
              console.log("error: ", ex);
            }            
          });

          //댓글 조회
          $.ajax({
            url: "/polaroid/posts/" + post_id + "/replies",
            type: 'GET',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            success: function (data) {
              getCommentList(data);
            },
            fail: function (ex) {
              console.log("error : ", ex);
            }
          });
        });



        //댓글 등록
        $('#addComment').on("click", function () {

          if ($('#replyContent').val() != "") {

            $.ajax({
              url: "/polaroid/posts/" + post_id + "/replies",
              type: 'POST',
              contentType: 'application/json;charset=utf-8',
              dataType: 'json',
              data: JSON.stringify({
                replyContent: $('#replyContent').val()
              }),

              success: function (data) {
                $('#replyContent').val("");
                getCommentList(data);
              },
              fail: function (ex) {
                console.log("error: ", ex);
              }
            });
          }
          else {
            alert("댓글 내용을 입력하세요.");
          }

        });

        //댓글 등록 엔터키 이벤트
        $('#replyContent').keydown(function (keynum) {
          if (keynum.keyCode == 13) { //엔터키 이벤트 발생

            if ($('#replyContent').val() != "") {

              $.ajax({
                url: "/polaroid/posts/" + post_id + "/replies",
                type: 'POST',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                data: JSON.stringify({
                  replyContent: $('#replyContent').val()
                }),

                success: function (data) {
                  $('#replyContent').val("");
                  getCommentList(data);
                },
                fail: function (ex) {
                  console.log("error: ", ex);
                }
              });
            }
            else {
              alert("댓글 내용을 입력하세요.");
            }
          }
        });


        //댓글 삭제
        $('#commentList').on("click", ".subComment", function () {

          let reply_id = $(this).closest("div").data("id");

          $.ajax({
            url: "/polaroid/posts/" + post_id + "/replies/" + reply_id,
            type: 'DELETE',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',

            success: function (data) {
              getCommentList(data);
            },

            fail: function (ex) {
              console.log("error: ", ex);
            }

          });

        });


        //댓글 수정 폼 생성
        $('#commentList').on("click", ".modComment", function () {

          modifyForm_reply_id = $(this).closest("div").data("id");

          $('#addForm').hide();
          $('#modifyForm').show();


        });

        //댓글 수정
        $('#modifyForm').on("click", '#ok', function () {
          let reply_id = modifyForm_reply_id;

          if ($('#modContent').val() != "") {
            $.ajax({
              url: "/polaroid/posts/" + post_id + "/replies/" + reply_id,
              type: 'PUT',
              contentType: 'application/json;charset=utf-8',
              dataType: 'json',
              data: JSON.stringify({
                replyContent: $('#modContent').val()
              }),

              success: function (data) {
                $('#modContent').val("");
                $('#modifyForm').hide();
                $('#addForm').show();
                getCommentList(data);
              },
              error: function (ex) {
                console.log(ex);
              }

            });
          }
          else {
            alert("댓글 내용을 입력하세요.");
          }

        });

        //댓글 수정 엔터키 이벤트
        $('#modContent').keydown(function (keynum) {
          let reply_id = modifyForm_reply_id;

          if (keynum.keyCode == 13) { //엔터키 이벤트 발생
            if ($('#modContent').val() != "") {

              $.ajax({
                url: "/polaroid/posts/" + post_id + "/replies/" + reply_id,
                type: 'PUT',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                data: JSON.stringify({
                  replyContent: $('#modContent').val()
                }),

                success: function (data) {
                  $('#modContent').val("");
                  $('#modifyForm').hide();
                  $('#addForm').show();
                  getCommentList(data);
                },
                error: function (ex) {
                  console.log(ex);
                }
              });
            }
            else {
              alert("댓글 내용을 입력하세요.");
            }
          }
        });


          //게시글 검색
          $('#keyword').keydown(function (keynum) {
            if (keynum.keyCode == 13) {

              if ($('#keyword').val() != "") {
                $.ajax({
                  url: "/polaroid/search",
                  type: 'POST',
                  contentType: 'application/json;charset=utf-8',
                  dataType: 'json',
                  data: JSON.stringify({
                    keyword: $('#keyword').val() //검색내용
                  }),

                  success: function (data) {
                    $('#keyword').val();
                    //기존에 있던 postList 없애기
                    $('#contents').empty();
                    getSearchPost(data);
                  },
                  error: function (ex) {
                    console.log("error", ex);
                  }
                });
              }
              else {
                alert("검색할 게시글 제목을 입력하세요.");
              }
            }

          });

          //게시글 좋아요
          $('#postLikeBtn').on('click', function () {
            let div = this;
            let css = $(div).find('#heart').attr("class");

            if (css.includes('bx-heart')) {  //좋아요 추가

              $.ajax({
                url: "/polaroid/postLike/" + post_id,
                type: 'GET',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                success: function (data) {
                  $(div).find('#heart').removeClass("bx bx-heart");   //빈 하트 사라짐
                  $(div).find('#heart').addClass("bx bxs-heart");     //하트 생김           
                  $('#postLikeCount').html(data.postLikeCount);
                },
                fail: function (ex) {
                  console.log("error: ", ex);
                }
              });

            } else {  //좋아요 취소
              $.ajax({
                url: "/polaroid/deletePostLike/" + post_id,
                type: 'GET',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                success: function (data) {
                  $(div).find('#heart').removeClass("bx bxs-heart");
                  $(div).find('#heart').addClass("bx bx-heart");
                  $('#postLikeCount').html(data.postLikeCount);
                },
                fail: function (ex) {
                  console.log("error: ", ex);
                }
              });

            }

          });

          //게시글 hover 출력 
          $(function() {

            $(".contents-card").on("mouseenter", function(e) {
              e.preventDefault();

                let post_id = $(this).closest('a').data('id');            

              let result = $(this);

              result.find('.hover-box').css('display','block');


              $.ajax({
                url: "/polaroid/posts/" + post_id,

                type: 'GET',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                success: function (data) {
                  result.find('#postLikeCount2').html(data.postLikeCount);                                      
                },
                fail: function(ex){
                  console.log("error: ", ex);
                }
              });

              $.ajax({
                  url: "/polaroid/posts/" + post_id + "/replies",
                  type: 'GET',
                  contentType: 'application/json;charset=utf-8',
                  dataType: 'json',
                  success: function (data) {
                    let replyList = data.replyList;
                    result.find('#replyCount').html(data.replyList.length);
                  },
                  fail: function (ex) {
                    console.log("error : ", ex);
                  }
              });
            }); 

            $(".contents-card").on("mouseleave", function() {
              $(this).find('.hover-box').css('display','none');
            });
          });



          //댓글 좋아요
          $('#commentList').on("click", ".replyLikeBtn", function (){
            let a = this;
            let css = $(a).find('#heart').attr("class");    //bs bx-heart
            let reply_id = $(a).data("id");                 //댓글 번호

            if (css.includes('bx-heart')) {                         //좋아요 추가
              $.ajax({
                url: "/polaroid/replyLike/" +post_id + "/" + reply_id,
                type: 'GET',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                success: function (data) {                
                  getCommentList(data);
                  
                  $(a).find('#heart').removeClass("bx bx-heart");   //빈 하트 사라짐
                  $(a).find('#heart').addClass("bx bxs-heart");     //하트 생김           
                },
                fail: function (ex) {
                  console.log("error: ", ex);
                }
              });

            } else {                                                      //좋아요 취소
              $.ajax({
                url: "/polaroid/deleteReplyLike/" + post_id + "/" + reply_id,
                type: 'GET',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                success: function (data) {
                  getCommentList(data);
              
                  $(a).find('#heart').removeClass("bx bxs-heart");
                  $(a).find('#heart').addClass("bx bx-heart");
                },
                fail: function (ex) {
                  console.log("error: ", ex);
                }
              });

            }

          });



              //게시글 검색 조회
              function getSearchPost(data){

                let posts = data.searchPostList;

                let htmlStr = "";

                htmlStr += "<style>.contents-img:nth-child(2),.contents-img:nth-child(3) {display:none;}</style>";
                  
                for(let i = 0; i < posts.length; i++){
                  htmlStr += "<div class='col-lg-4 col-md-4 col-4 mb-4'>";
                  htmlStr += "<div class='card'>";
                  htmlStr += "<a href='#' type='button' class='' data-bs-toggle='modal' data-bs-target='#modalScrollable' data-id=" + posts[i].post_id + ">";
                  htmlStr += "<div class='card-body contents-card'>";           
                  for(let j = 0; j < posts[i].uploads.length; j++){                       
                    htmlStr += "<div class='contents-img' style=\"background:url('/upload/" + posts[i].uploads[j].upload_filepath + "/" + posts[i].uploads[j].upload_fileuuid + "_" + posts[i].uploads[j].upload_filename + "') no-repeat center; background-size: cover;\">";   
                    htmlStr += "<div class='hover-box'><i class='bx bxs-heart'><span id='postLikeCount2'></span></i>&nbsp;&nbsp;";
                    htmlStr += "<i class='bx bxs-chat'><span id='replyCount'></span></i>";
                    htmlStr += "</div></div>";
                  }                
                  
                  htmlStr += "</div></a></div></div>";
                }
                $('#contents').html("");
                $('#contents').append(htmlStr);
              

                $(".contents-card").on("mouseenter", function(e) {
                  e.preventDefault();
                                      
                  let post_id = $(this).closest('a').data('id');            
                               
                  let result = $(this);
                  
                  result.find('.hover-box').css('display','block');
                  
                  $.ajax({
                    url: "/polaroid/posts/" + post_id,
                    type: 'GET',
                    contentType: 'application/json;charset=utf-8',
                    dataType: 'json',
                    success: function (data) {  
                      result.find('#postLikeCount2').html(data.postLikeCount);
                    },
                    fail: function(ex){
                      console.log("error: ", ex);
                    }
                  });

                  $.ajax({
                    url: "/polaroid/posts/" + post_id + "/replies",
                    type: 'GET',
                    contentType: 'application/json;charset=utf-8',
                    dataType: 'json',
                    success: function (data) {
                      let replyList = data.replyList;
                      result.find('#replyCount').html(data.replyList.length);
                    },
                    fail: function (ex) {
                      console.log("error : ", ex);
                    }
                  });
                   
                });
               
                $(".contents-card").on("mouseleave", function() {
                  $(this).find('.hover-box').css('display','none');
                });
              }


              //게시글 제목 조회
              function getPostTitle(data) {
                  let post = data.post;
            
                  let htmlStr = "";

                  htmlStr += "<h5 class='modal-title' id='modalScrollableTitle'>"+ post.post_title +"</h5>";
                  htmlStr += "<button type='button' class='btn-close' data-bs-dismiss='modal' aria-label='Close'></button>";              

                  $('#postTitle').html("");
                  $('#postTitle').append(htmlStr);

              }

              //게시글 이미지 상세 조회
              function getPostPhoto(data) {
                let post = data.post;
                
                let htmlStr = "";


                for (let i = 0; i < post.uploads.length; i++) {
                  htmlStr += "<div class='carousel-item active'>";
                  htmlStr += "<img class='d-block w-100' src=@{'/upload/" + post.uploads[i].upload_filepath + "/" + post.uploads[i].upload_fileuuid + "_" + post.uploads[i].upload_filename + "}\'} alt='Third slide'  />";
                  htmlStr += "</div>";
                }

                $('#postPhoto').html("");
                $('#postPhoto').append(htmlStr);
                $('.carousel-item:nth-child(2),.carousel-item:nth-child(3)').removeClass('active');
              }    
           
              //게시글 작성자 조회
              function getPostAccount(data) {
                let post = data.post;

                let htmlStr = "";

                htmlStr += "<div class='profile-img' style='background: url(img/avatars/1.jpg) no-repeat center; background-size: cover;'></div>";
                htmlStr += "<div class='user_container'>";
                htmlStr += "<div class='user_name'>";
                htmlStr += "<div class='nick_name'>" + post.member_nick + "</div>";
                htmlStr += "</div>";
                htmlStr += "<div class='country'>" + post.post_content + "</div>";
                htmlStr += "</div>";

                $('#postAccount').html("");
                $('#postAccount').append(htmlStr);
              }

              //게시글 수정,삭제 조건
              function getBtn(data) {
                let post = data.post;

                let htmlStr = "";

                htmlStr += "<div class='user-control'>";
                if (post.member_nick == memberNick) {
                  htmlStr += "<button id='modifyBtn' type='button' class='btn btn-primary edit-btn modifyPost'><a style='color:white'>수정</a></button>";
                  htmlStr += "<button id='removeBtn' type='button' class='btn btn-primary delete-btn deletePost'><a style='color:white'>삭제</a></button>";
                }
                htmlStr += "</div>";

                $('#btnCondition').html("");
                $('#btnCondition').append(htmlStr);

                //게시글 수정
                $('#modifyBtn').on("click", function () {
                  location.href = "/polaroid/updateForm?post_id=" + post_id;
                });

                //게시글 삭제
                $('#removeBtn').on("click", function () {
                  location.href = "/polaroid/deletePost?post_id=" + post_id;
                });
              }


              //게시글 좋아요
              function getPostLike(data) {
              
                let post = data.post;
                

                let htmlStr1 = "<a href='#'><i class='bx bx-heart'></i></a>";
                let htmlStr = "좋아요<span class='count'>"+ post.likeCount + "</span>개";
                
                $('#postLikeCount, #postLikeBtn').html("");
                $('#postLikeBtn').append(htmlStr1);
                $('#postLikeCount').append(htmlStr);
              }


              //댓글 리스트 조회
              function getCommentList(data) {

                let replyList = data.replyList;

                let htmlStr = "";

                for (let i = 0; i < replyList.length; i++) {
                  htmlStr += "<div class='admin_container' id=" + replyList[i].replyId + ">";
                  htmlStr += "<div class= 'profile-img' style='background: url(img/avatars/1.jpg) no-repeat center; background-size: cover;'></div>";
                  
       /*            if (isProfile == 0) {
              alert("" + htmlStr);
                  htmlStr += "<div class='profile-img' style='background: url(/img/avatars/1.jpg) no-repeat center; background-size: cover;'></div>";
                } else {
            htmlStr += "<div class='profile-img' style=\"background:url('/upload/" + replyList[i].member_id.profileFilePath + "/" + replyList[i].member_id.profileFileUuid + "_" + replyList[i].member_id.profileFileName + "') no-repeat center; background-size: cover;\"></div>";     
          }*/

                  
                  htmlStr += "<div class='comment'>";
                  htmlStr += "<span class='user_id'>" + replyList[i].memberNick + "</span>" + "<div>" + replyList[i].replyContent + "</div>";
                  htmlStr += "<div class='time'>" + replyList[i].replyRegdate + "</div>";
                  htmlStr += "<div class='time reply-btn' data-id=" + replyList[i].replyId + ">";
                  
                  console.log(i + "번째 세션아이디: ", data.member_id);
                  console.log("현재 세션아이디: ", memberId);                  
                  
                  if (replyList[i].memberNick == memberNick) {
                    htmlStr += "<a class='modComment'>수정</a>";
                    htmlStr += "<a class='subComment'>삭제</a>";
                  }
                  htmlStr += "</div>";
                  htmlStr += "<div class='reply_edit'><a href='#n'></a></div>";
                  htmlStr += "<div class='reply_delete'><a href='#n'></a></div>";
                  htmlStr += "<div class='reply_heart_btn' class='replyheartBtn'>";
                  if(replyList[i].isReplyLike == 0){
                    htmlStr += "<a href='#' class='replyLikeBtn' data-id=" + replyList[i].replyId + "><i id='heart' class='bx bx-heart'></i></a>";
                  }
                  else{
                    htmlStr += "<a href='#' class='replyLikeBtn' data-id=" + replyList[i].replyId + "><i id='heart' class='bx bxs-heart'></i></a>";
                  }
                  htmlStr += "<span class='replyLikeBtn'>" + replyList[i].replyLikeCount + "</span>"; 
                  htmlStr += "</div></div></div>";
                }

                $('#commentList').html("");
                $('#commentList').append(htmlStr);

              }


        });
    
      /*]]>*/