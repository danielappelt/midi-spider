# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
