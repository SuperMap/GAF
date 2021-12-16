#! /bin/sh

function merge_artifacts(){
  rm -fr ./dist/**
  for file in `ls packages`
  do
    if [ -d packages/$file ];then
      if [ "$file" = "portal" ]; then
        cp -r packages/portal/dist/** dist
      elif [ "$file" != "test" ];then
        mkdir -p dist/apps/apps-$file
        cp -r packages/$file/dist/** dist/apps/apps-$file
      fi
    fi
  done
}

merge_artifacts
