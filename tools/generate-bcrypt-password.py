# This script prompts for a password and outputs its hash using bcrypt.
# The script can be used to generate a password hash for Prometheus.
import getpass
import bcrypt

password = getpass.getpass("password: ")
hashed_password = bcrypt.hashpw(password.encode("utf-8"), bcrypt.gensalt())
print(hashed_password.decode())
