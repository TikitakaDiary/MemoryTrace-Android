# MemoryTrace-Android
[![Platform](https://img.shields.io/badge/platform-Android-green.svg) ]()
[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![Language](https://img.shields.io/github/languages/top/Nexters/Yetda_Android)]()

# Project Management
## Issue
* 기능/작업별로 [이슈](https://github.com/TikitakaDiary/MemoryTrace-Android/issues)를 생성한다.
* 모든 이슈는 [프로젝트](https://github.com/TikitakaDiary/MemoryTrace-Android/projects/1)에서 관리된다.
* PullRequest를 이용해 해당 이슈를 종료하는 것을 우선으로 한다.
* 기본적으로 [템플릿](https://github.com/TikitakaDiary/MemoryTrace-Android/tree/main/.github/ISSUE_TEMPLATE/)을 이용하여 작성한다.
* 라벨을 통해 분류된다.
  ```
  Feature : 기능개발
  Bug : 버그 픽스
  Docs : 문서작업
  Refactor : 개선
  Test : 테스트 코드
  보류 : 보류, 논의필요

  In Progrss : 작업 중입니다.
  작업자명 : 해당 이슈를 작업중인 사람
  ```

<br/>

## Commit Message
- [템플릿](https://github.com/TikitakaDiary/MemoryTrace-Android/tree/main/.github/COMMIT_TEMPLATE.txt/)을 이용하여 작성한다.
- 최대한 기능단위로 쪼개서 커밋한다.
```
라벨 목록
- feature   : 새로운 기능
- bug       : 버그 수정
- refactor  : 리팩토링
- docs      : 문서(추가, 수정, 삭제)
- test      : 테스트
- etc       : 기타 변경 사항
```
> 커밋 템플릿 사용시 참고용 vi 기본 명령어
> - a : 현재 커서 다음위치에 삽입 (입력모드로 넘어감)
> - o : 현재 줄 다음위치에 삽입 (입력모드로 넘어감)
> - esc : 작성완료 후 입력모드에서 빠져 나오기
> - x : 커서가 위치한 곳의 글자 1개 삭제
> - dd : 커서가 위차한 곳의 한줄 삭제
> - :q! : 저장하지 않고 나가기
> - wq : 저장 후 나가기 (커밋메세지 작성완료)

<br/>

## Pull Request
* 기본적으로 [템플릿](https://github.com/TikitakaDiary/MemoryTrace-Android/tree/main/.github/PULL_REQUEST_TEMPLATE.txt)을 이용하여 작성한다.
* 작업한 사람은
   1. PR을 생성하고
   2. 리뷰를 요청하고
   3. 리뷰가 approve되면 머지하고
   4. 작업한 브랜치를 삭제한다.
  
* 리뷰어는
    1. 성심성의껏 코드를 리뷰하고 😎
    2. 리뷰 완료 후 해당 커밋을 approve 한다.

* 라벨을 통해 분류된다.
  ```
    Simple : 간단한 코드수정으로 리뷰어가 빠르게 리뷰할 수 있을 경우
    Test Needed : 추가로 테스트 필요한 경우
    Review Needed : 리뷰가 필요한 경우
    Merge Needed : 해당 PR이 approve되어 머지 가능한 경우
  ``` 

<br/>

## Tech Stack & References
_작성필요_

<br/>

## 기타 설정 방법
* 라벨은 [json](https://github.com/TikitakaDiary/MemoryTrace-Android/tree/main/.github/label.json) 파일로 추가하였다.
> npx github-label-sync — access-token [액세스 토큰] — labels labels.json [계정명]/[저장소 이름]

* 커밋 템플릿은 다음과 같이 설정할 수 있다.
> git config commit.template ./.github/COMMIT_TEMPLATE.txt

