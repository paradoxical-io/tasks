function snapshot() {
  sbt -Dversion="${TRAVIS_BUILD_NUMBER}-SNAPSHOT" publishSigned
}

function release() {
  sbt -Dversion=$REVISION publishSigned sonatypeRelease
}
