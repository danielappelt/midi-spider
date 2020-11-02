# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- Upgrade re-frame-template to 1.3.0
  - Upgrade [lein-shadow to 0.3.1](https://gitlab.com/nikperic/lein-shadow/-/blob/master/CHANGELOG.md#030-2020-09-22)
  - Upgrade shadow-cljs to 2.11.4
  - Upgrade karma to 5.2.3
  - Upgrade [re-frame to 1.1.1](http://day8.github.io/re-frame/releases/2020/#110-2020-08-24)
  - Upgrade binaryage/devtools to 1.0.2
  - Upgrade ClojureScript 1.10.773

## [0.2.1] - 2020-06-21

### Fixed

- Provide all messages in input buffer as download
- Fix HTML link element

### Changed

- Move test functions to util-test namespace
- Move utility functions to util namespace
- Improve MIDI error message
- Upgrade node dependencies

## [0.2.0] - 2020-06-19

### Fixed

- Fix docs and add feature checks (closes #1)

### Changed

- Keep all messages received since last send operation
- Add comments and remove unused method in subs.cljs
- Remove duplicate singleRun config option
- Update karma to 5.1.0

### Added

- Add first tests
- Provide information in the About panel

## [0.1.0] - 2020-06-13

### Added

- Add CHANGELOG
- Download last received data as binary file
- Allow manual data entry
- Allow loading of binary sysex files (*.syx)
- Two-pane layout with selectable MIDI input and output
- Send arbitrary data to MIDI devices
- Monitor incoming data from MIDI devices

[unreleased]: https://github.com/danielappelt/midi-spider/compare/v0.2.1...HEAD
[0.2.1]: https://github.com/danielappelt/midi-spider/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/danielappelt/midi-spider/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/danielappelt/midi-spider/releases/tag/v0.1.0
