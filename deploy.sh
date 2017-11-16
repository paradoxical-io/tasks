function snapshot() {
  sbt -Dversion="1.0.${TRAVIS_BUILD_NUMBER}" publishSigned sonatypeReleaseAll
}

function release() {
  sbt -Dversion=$REVISION publishSigned sonatypeReleaseAll
}
