{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use newtype instead of data" #-}
{-# HLINT ignore "Replace case with fromMaybe" #-}
-- These directives simply omit some linter warnings 


module MultiSet (
   MSet(..),
   add,
   addList,
   empty,
   diff,
   union,
   occs,
   elems,
   subeq,
   mapMSet,
   basictest) where

import Data.List (foldl')

data MSet a = MS [(a,Int)] deriving (Show)

instance Eq a => Eq (MSet a) where
   (==) :: Eq a => MSet a -> MSet a -> Bool
   (MS a) == (MS b) =   all (\(x,m) -> occs (MS b) x == m) a &&
                        all (\(x,m) -> occs (MS a) x == m) b

-- diff was not requested by the assignment
-- it was used only for debugging purposes
-- 
-- | Given two MSet A,B finds all differences in the sets
--    @param A set to be compared
--    @param B set to be compared
--    @returns all keys which have a 
--             different multiplicity in A and B in a format like
--             [(key,A[key],B[key])] 

diff :: Eq a => MSet a -> MSet a -> [(a, Int, Int)]
diff (MS a) (MS b) =   [(x,m,mb) | (x,m) <- a, let mb = occs (MS b) x, m /= mb]
                     ++ [(x,0,mb) | (x,mb) <- filter (\(x,m) -> x `notElem` map fst a) b]

-- Notice that foldr is applied only to the keys of the multiset, since
-- it is defined as `Mset a`. No operation on the multiplicities is performed
instance Foldable MSet where
   foldr f z (MS []) = z
   foldr f z (MS ((x, m):xs)) = f x (foldr f z (MS xs))

-- | callNTimes, applies n times f to x
--    @param f function to be applied
--    @param n number of times to apply f
--    @param x argument on which f should be appliead
callNTimes :: (Ord t1, Num t1) => t1 -> (t2 -> t2) -> t2 -> t2
callNTimes n f x
  | n <= 0    = x
  | otherwise = callNTimes (n - 1) f (f x)

callNTimes' :: Int -> (c -> c) -> c -> c
callNTimes' n f = foldl1 (.) (replicate n f)

-- empty, that returns an empty MSet
empty :: MSet a
empty = MS []

-- add mset v, returning a multiset obtained by adding the element v to mset.
-- Clearly, if v is already present its multiplicity has to be increased by one, otherwise it
-- has to be inserted with multiplicity 1.
-- add :: Eq a => MSet a -> a -> MSet a
add :: Eq a => MSet a -> a -> MSet a
add (MS mset) v = if v `notElem` elems (MS mset) then MS ((v,1):mset)
   else MS [(x,if x == v then m + 1 else m) | (x,m) <- mset]
-- Infix  add operator
(.+) :: Eq a => MSet a -> a -> MSet a
(.+) = add

-- Adds a list of keys to a MultiSet exploiting add.
-- useful only for readability purposes
addList :: Eq a => MSet a -> [a] -> MSet a
addList (MS mset) [] = MS mset
addList (MS mset) (x:xs) = addList (add (MS mset) x) xs

-- occs mset v, returning the number of occurrences of v in mset (an Int).
occs :: Eq a => MSet a -> a -> Int
occs (MS mset) v = case lookup v mset of
   Just mul -> mul
   Nothing  -> 0

-- elems mset, returning a list containing all the elements of mset.
elems :: MSet a -> [a]
elems (MS mset) = map fst mset

-- subeq mset1 mset2, returning True if each element of mset1 is also an element
-- of mset2 with the same multiplicity at least.

subeq :: Eq a => MSet a -> MSet a -> Bool
subeq (MS mset1) (MS mset2) = all (\(x,mul) -> occs (MS mset2) x >= mul) mset1

-- union mset1 mset2, returning an MSet having all the elements of mset1 and
-- of mset2, each with the sum of the corresponding multiplicites.
union :: Eq a => MSet a -> MSet a -> MSet a
union (MS mset1) (MS mset2) =
   MS ([(x,m + occs (MS mset2) x) | (x,m) <- mset1] ++ filter (\(x,m) -> x `notElem` map fst mset1) mset2)
-- Infix union operator
(.++) :: Eq a => MSet a -> MSet a -> MSet a
(.++) = union


-- These two have to same behaviour of add and addList, but work on lists, not on MSet
-- They are needed for mapMSet
addToMultiset :: (Eq a, Num b) => [(a, b)] -> a -> [(a, b)]
addToMultiset mset v = if v `notElem` map fst mset then (v,1) : mset
   else [(x,if x == v then m + 1 else m) | (x,m) <- mset]

-- These two addListToMultiset definitions are equal
-- inferred type signature differs a bit
addListToMultiset :: (Eq a, Num b) => [(a, b)] -> [a] -> [(a, b)]
-- addListToMultiset l [] = l
-- addListToMultiset l (x:xs) = addListToMultiset (addToMultiset l x) xs
-- addListToMultiset :: [(Integer, Int)] -> [Integer] -> [(Integer, Int)]
addListToMultiset = foldl addToMultiset

mapMSet f (MS mset) = MS (foldl (\acc (v,n) -> addListToMultiset acc (replicate n (f v))) [] mset)

{-
-------------------------------------------------------------------------------------------------------
------------------------------------------------FUNCTOR------------------------------------------------
-------------------------------------------------------------------------------------------------------


-- instance Functor MSet where
--    fmap f (MS mset) = mapMSet f (MS mset)
The previous statement throws an error:
MSet in general cannot correctly instantiate Functor, since MSet requires the key type
to instantiate the Eq typeclass, so a correct fmap implementation would have the following signature
   fmap::Eq b => (a -> b) -> MSet a -> MSet b
which does not allow to instantiate Functor, which requires the signature for fmap to be unconstrained
    fmap :: (a -> b) -> f a -> f b
-}

-- Basic test for debugging
-- ms = add empty 1
ms :: MSet Integer
ms = empty .+ 1 .+ 1 .+ 2 .+ 3 .+ 5 .+ 5 .+ 1
ms2 :: MSet Integer
ms2 = empty .+ 2 .+ 1 .+ 2 .+ 5 .+ 3 .+ 5 .+ 1 .+ 1
ms3 = callNTimes 6 (.+ 1) empty .++ callNTimes' 4 (.+ 12) empty .+ 5 .+ 3 .+ 5

basictest = do
   print ms
   print ms3
   let ms4 = mapMSet (*2) ms3
   let f = (`mod` 11)
   let g = (`mod` 4)
   let comp = f . g
   print ms4
   print $ mapMSet f (mapMSet g ms4) == mapMSet comp ms4

   let exp_out = "\n---Expected output:\nMS [(5,2),(3,1),(2,1),(1,3)]\nMS [(3,1),(5,2),(1,6),(12,4)]\nMS [(24,4),(2,6),(10,2),(6,1)]\nTrue"
   -- putStr to evaluate the \n
   putStr exp_out
