\# redis-clone-java



A subset of a Redis server written from scratch in Java — no Redis libraries, just sockets and the RESP wire protocol. It speaks real RESP, so it works with the actual `redis-cli` client.



\## Supported commands



| Command | Behaviour |

|---|---|

| `PING` | Replies `PONG` |

| `ECHO <msg>` | Returns the message back |

| `SET <key> <value>` | Stores a key, replies `OK` |

| `SET <key> <value> PX <ms>` | Stores a key that expires after `<ms>` milliseconds |

| `GET <key>` | Returns the value, or nil if missing or expired |



Unknown commands return an error reply.



\## Running it



&#x20;   mvn.cmd compile

&#x20;   java -cp target\\classes Main



The server listens on port 6379. Connect with any Redis client:



&#x20;   redis-cli



Run the tests with `mvn.cmd test`.



\## How it works



`Main` opens a `ServerSocket` on 6379 and loops on `accept()`. Each accepted socket is handed to an `ExecutorService`, which runs a `ClientHandler` on its own thread — so one slow client can't block the others. Handlers share a single static `ConcurrentHashMap`, which gives thread-safe reads and writes without locking the whole map.



Commands arrive as RESP arrays. `RespParser` reads the `\*N` element count, then for each element reads the `$N` length header followed by the payload. Parsing off the declared length rather than splitting on whitespace is what allows values containing spaces to work correctly.



Expiry is lazy. `SET ... PX` records an absolute expiry timestamp on the entry rather than a countdown, and nothing checks it until someone reads that key — `GET` calls `lookup`, which deletes the entry and returns nil if it has passed. The tradeoff: no background sweeper thread and no cost for keys nobody touches, but expired keys keep occupying memory until something reads them. Real Redis does both, sampling keys periodically as well as checking on read.



\## Tests



Six JUnit 5 tests covering the parser (single and multi-element commands, empty input, values containing spaces) and expiry (a live key returns its value; an expired key returns nil and is removed from the map). The expiry tests construct entries with absolute timestamps rather than sleeping, so they run instantly and don't flake.



\## What's next



\- The parser reads through a `Reader`, which limits values to text. RESP is length-prefixed precisely so bulk strings can carry binary data or embedded CRLF — moving to `InputStream` would fix that.

\- RDB persistence, so data survives a restart

\- More commands: `DEL`, `EXISTS`, `INCR`, `TTL`

\- Replication



\## Local notes



Running the server on Windows and `redis-cli` in WSL: WSL can't reach Windows on localhost, so connect with `redis-cli -h <windows-ip>`. Find the IP with `ip route show | grep default`; it changes after a reboot.

