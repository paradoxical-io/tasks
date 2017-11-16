function snapshot() {
  sbt -Dversion="1.0.${TRAVIS_BUILD_NUMBER}" publishSigned sonatypeRelease
}

function release() {
  sbt -Dversion=$REVISION publishSigned sonatypeRelease
}
