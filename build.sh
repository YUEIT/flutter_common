buildFlutter() {
  cd android_build || exit
  ./gradlew buildFlutter
}

buildFlavor() {
  rm -rf build
  if test "$#" == 0
  then
    flutter build apk --no-sound-null-safety --flavor=product
  else
    flutter build apk --no-sound-null-safety
  fi
  buildFlutter
}

for line in $(cat local.properties)
do
  if [[ $line =~ flutter.build ]]
  then
    if test ${line#*=} = release
    then
      buildFlavor true
    else
      buildFlavor
    fi
  fi
done

