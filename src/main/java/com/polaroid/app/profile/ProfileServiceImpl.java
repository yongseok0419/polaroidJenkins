package com.polaroid.app.profile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;
import com.polaroid.app.command.ProfileDto;

import net.coobird.thumbnailator.Thumbnailator;


@Transactional(readOnly = true)
@Service("profileService")
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	ProfileMapper profileMapper;
	
	@Value("${project.upload.path}")
	private String upload_filepath;
	
	//폴더생성함수
	public String makeFolder() {
		String path = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		File file = new File(upload_filepath + "\\" + path);
		if(file.exists() == false) {
			file.mkdirs();	//파일 생성
		}
		return path;	//경로
	}
	
	//프로필 등록
	@Transactional
	@Override
	public boolean registerProfile(ProfileDto profileDto, MemberDto memberDto, MultipartFile upload) {
		
		//1. 파일업로드
		
			//실제 파일명 (브라우저별로 조금씩 다를 수 있음)
			String origin = upload.getOriginalFilename();
			
			System.out.println(origin.toString());
			//저장할 파일명 (경로가 \\가 들어오는 경우 잘라서 처리)
			String filename = origin.substring(origin.lastIndexOf("\\") + 1);
			//파일사이즈
			long size = upload.getSize();
			//랜덤 이름
			String uuid = UUID.randomUUID().toString();
			//날짜 경로
			String path = makeFolder();
			//업로드 경로
			String saveName = upload_filepath + "\\" + path + "\\"+ uuid + "_" + filename;
			//썸네일 경로
			String thumbnailName = upload_filepath + "\\" + path + "\\thumb_" + uuid + "_" + filename;
			
			System.out.println("파일명 : " + filename);
			System.out.println("파일크기 : " + size);
			System.out.println("저장명 : " + saveName);
			
			try {
				File saveFile = new File(saveName);
				upload.transferTo(saveFile); // 파일업로드
				// 썸네일 생성 업로드
				Thumbnailator.createThumbnail(saveFile, new File(thumbnailName), 160, 160);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("업로드중 에러 발생");
			}
			
			//2. 닉네임만 변경
			profileMapper.updateMemberNick(memberDto);
			
			// 3. 프로필 등록
			profileMapper.insertProfileFile(profileDto.builder()
								.profileFileName(origin)
								.profileFilePath(path)
								.profileFileUuid(uuid)
								.profileAccount(profileDto.getProfileAccount())
								.profileWebsite(profileDto.getProfileWebsite())
								.profileAboutMe(profileDto.getProfileAboutMe())
								.memberId(profileDto.getMemberId())
								.build());
			
		return true;
	}
	
	//프로필 등록(이미지를 선택하지 않은 경우)
		@Transactional
		@Override
		public void registProfileExcludeImage(ProfileDto profileDto, MemberDto memberDto) {
			//1. 닉네임만 변경
			profileMapper.updateMemberNick(memberDto);
			
			//2. 프로필 등록
			profileMapper.insertProfileFile(profileDto.builder()
								.profileAccount(profileDto.getProfileAccount())
								.profileWebsite(profileDto.getProfileWebsite())
								.profileAboutMe(profileDto.getProfileAboutMe())
								.memberId(profileDto.getMemberId())
								.build());
		}

	//프로필 조회
	@Override
	public MemberProfileDto retrieveProfileDetail(int memberId) {
		return profileMapper.selectProfileDetail(memberId);
	}
	
	//프로필 존재유무
	@Override
	public int isProfile(int memberId) {
		return profileMapper.isProfile(memberId);
	}
	
	//프로필 수정(이미지를 선택한 경우)
	@Transactional
	@Override
	public void modifyProfile(ProfileDto profileDto, MemberDto memberDto, MultipartFile upload) {
		
		//1. 파일업로드
		
		//실제 파일명 (브라우저별로 조금씩 다를 수 있음)
		String origin = upload.getOriginalFilename();
		
		System.out.println(origin.toString());
		//저장할 파일명 (경로가 \\가 들어오는 경우 잘라서 처리)
		String filename = origin.substring(origin.lastIndexOf("\\") + 1);
		//파일사이즈
		long size = upload.getSize();
		//랜덤 이름
		String uuid = UUID.randomUUID().toString();
		//날짜 경로
		String path = makeFolder();
		//업로드 경로
		String saveName = upload_filepath + "\\" + path + "\\"+ uuid + "_" + filename;
		//썸네일 경로
		String thumbnailName = upload_filepath + "\\" + path + "\\thumb_" + uuid + "_" + filename;
		
		System.out.println("파일명 : " + filename);
		System.out.println("파일크기 : " + size);
		System.out.println("저장명 : " + saveName);
		
		try {
			File saveFile = new File(saveName);
			upload.transferTo(saveFile); // 파일업로드
			// 썸네일 생성 업로드
			Thumbnailator.createThumbnail(saveFile, new File(thumbnailName), 160, 160);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("업로드중 에러 발생");
		}
		
		//2. 닉네임, 비밀번호 변경
		profileMapper.updateMember(memberDto);
		
		//3. 프로필 수정
		profileMapper.updateProfile(profileDto.builder()
							.profileFileName(origin)
							.profileFilePath(path)
							.profileFileUuid(uuid)
							.profileAccount(profileDto.getProfileAccount())
							.profileWebsite(profileDto.getProfileWebsite())
							.profileAboutMe(profileDto.getProfileAboutMe())
							.memberId(profileDto.getMemberId())
							.build());
	}
	
	//프로필 수정(이미지를 선택하지 않은 경우)
	@Transactional
	@Override
	public void modifyProfileExcludeImage(ProfileDto profileDto, MemberDto memberDto) {
		
		//1. 닉네임, 비밀번호 변경
		profileMapper.updateMember(memberDto);
		
		//2. 프로필 수정
		profileMapper.updateProfileExcludeImage(profileDto.builder()
							.profileAccount(profileDto.getProfileAccount())
							.profileWebsite(profileDto.getProfileWebsite())
							.profileAboutMe(profileDto.getProfileAboutMe())
							.memberId(profileDto.getMemberId())
							.build());
	}
	
	//메인 화면 회원 정보 조회
	@Override
	public MemberProfileDto retrieveMemberList(int memberId ) {
		return profileMapper.selectMemberList(memberId);
	}

}
