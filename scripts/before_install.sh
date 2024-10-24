#!/bin/bash

# 압축 파일 경로
TAR_FILE="/home/ec2-user/app.tgz"

# 압축을 해제할 경로
EXTRACT_TO="/home/ec2-user/extracted-folder"

# 압축 파일이 존재하는지 확인
if [ -f "$TAR_FILE" ]; then
  echo "압축 해제 중: $TAR_FILE"
  mkdir -p "$EXTRACT_TO"  # 압축 해제 경로 생성
  tar -xzf "$TAR_FILE" -C "$EXTRACT_TO"  # 파일 압축 해제
  echo "압축 해제 완료"
else
  echo "$TAR_FILE 파일을 찾을 수 없습니다."
  exit 1  # 압축 파일이 없으면 오류로 종료
fi
