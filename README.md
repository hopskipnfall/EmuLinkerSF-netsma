![Maintained][maintained-badge]
[![build](https://github.com/hopskipnfall/EmuLinker-K/actions/workflows/gradle.yml/badge.svg)](https://github.com/hopskipnfall/EmuLinker-K/actions/workflows/gradle.yml)
[![Crowdin](https://badges.crowdin.net/emulinker-k/localized.svg)](https://crowdin.com/project/emulinker-k)

[![Watch on GitHub][github-watch-badge]][github-watch]
[![Star on GitHub][github-star-badge]][github-star]
[![Tweet][twitter-badge]][twitter]

# EmuLinker-K

EmuLinker-K (ELK) is a server that uses the Kaillera protocol to facilitate online multiplayer for emulators.

EmuLinker-K is a Kotlin rewrite of [EmulinkerSF](https://github.com/God-Weapon/EmuLinkerSF), with an emphasis on measuring and improving performance, patching security and privacy vulnerabilities, and adding useful features for both server owners and users.  EmuLinker-K is maintained by [nue](https://twitter.com/6kRt62r2zvKp5Rh).

Please help us translate the server into more languages by contributing to our [CrowdIn project](https://crowdin.com/project/emulinker-k)

Feel free to file bugs and feature requests on this repository, or find our channel in the Kaillera Reborn discord:

[![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/MqZEph388c)

## Setting up a new server

Download the latest version from the [Releases](https://github.com/hopskipnfall/EmuLinker-K/releases/latest) page. After unzipping, make sure Java 17 or above is installed before run the following commands:

1. Allow scripts to be executed:

```bash
chmod +x *.sh
```

2. Run the server:

```bash
./start_server.sh
```

The server should now be running on port `27888`.

## Features

### Experimental/beta features

These features may be removed or changed without warning.

#### Twitter integration

You can have the server make a Twitter post when a user opens a game, and either delete it or reply marking it as "closed" when the game starts. At the time of writing this, both options are supported by the Twitter API free tier.

![image](https://user-images.githubusercontent.com/5498859/142763676-eaa6afdb-d521-4860-966d-a5c02246b561.png)

To set it up, configure the following values in `config/emulinker.cfg`:

```
# Twitter reporting integration switch. When enabled it will
# broadcast new open games.
twitter.enabled=true

# Delay (in seconds) before sending a tweet.
twitter.broadcastDelaySeconds=20

# Comma-separated list of phrases that, if found in the name
# after a "@", will prevent tweet posting.
# Example username: nue@waiting
twitter.preventBroadcastNameSuffixes=waiting,restart
# If true, will simply delete the tweet when the game starts.
twitter.deletePostOnClose=false

# You will need to make a new Twitter API app and fill in these values.
twitter.auth.oAuthAccessToken=
twitter.auth.oAuthAccessTokenSecret=
twitter.auth.oAuthConsumerKey=
twitter.auth.oAuthConsumerSecret=
```

You will also need to configure some messages in `config/language.properties`:

```
KailleraServerImpl.TweetPendingAnnouncement=Posting a tweet in {0} seconds. Type \"/stop\" to disable.
KailleraServerImpl.TweetCloseMessage=(opponent found)
KailleraServerImpl.CanceledPendingTweet=Canceled pending tweet.
```

With these settings, users whose name ends in @waiting (meaning they are waiting for a specific person to join their game) or @restart (meaning they are restarting the game and are waiting for the same person to join) will not have tweets sent. Similarly, users will be notified and given 20 seconds to type `/stop` to stop the tweet from sending.  After the game starts, the account will respond to the original tweet with the text "(opponent found)".

## Contributing

If you're interested in contributing to this project, please read [Contributing.md](CONTRIBUTING.md).

[prs-badge]: https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square
[prs]: http://makeapullrequest.com
[github-watch-badge]: https://img.shields.io/github/watchers/hopskipnfall/EmuLinker-K.svg?style=social
[github-watch]: https://github.com/hopskipnfall/EmuLinker-K/watchers
[github-star-badge]: https://img.shields.io/github/stars/hopskipnfall/EmuLinker-K.svg?style=social
[github-star]: https://github.com/hopskipnfall/EmuLinker-K/stargazers
[twitter]: https://twitter.com/intent/tweet?text=https://github.com/hopskipnfall/EmuLinker-K%20%F0%9F%91%8D
[twitter-badge]: https://img.shields.io/twitter/url/https/github.com/hopskipnfall/EmuLinker-K.svg?style=social
[maintained-badge]: https://img.shields.io/badge/maintained-yes-brightgreen
