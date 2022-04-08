{
  description = "A todo list with DnD style gamification";

  inputs = {
    flake-utils.url = "github:numtide/flake-utils";
    sbt-derivation.url = "github:zaninime/sbt-derivation";
  };
  outputs = { self, nixpkgs, flake-utils, sbt-derivation }@inputs:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ sbt-derivation.overlay ];
        };
        packageName = "PersonalGamification";
        dockerRepository = "collinarnett";
        version = "0.1.0";
        build = pkgs.sbt.mkDerivation {
          pname = "${packageName}";
          version = "${version}";
          depsSha256 = "pp85kq80/cq03+huBjURytxVw9HPoj4x3UK1Nb8dnoA=";
          src = ./.;
          buildPhase = ''
            sbt assembly
          '';
          installPhase = ''
            mkdir -p $out/bin
            cp target/scala-*/*-assembly-*.jar $out/bin/
          '';
        };
      in {
        packages.${packageName} = build;

        defaultPackage = self.packages.${system}.${packageName};
        packages.dockerImage = pkgs.dockerTools.buildLayeredImage {
          name = "${dockerRepository}/${packageName}";
          config.Entrypoint = [
            "${pkgs.jre_headless}/bin/java"
            "-jar"
            "${build}/bin/${packageName}-assembly-${version}.jar"
          ];
        };
        devShell = import ./shell.nix { inherit pkgs; };
      });
}
